/*
 * We want to replace how CommunicationMod handles ChoiceScreenUtils:makeMapChoice(int choice_index) because it adds too much latency
 * 
 * TODO: Replicate the logic for the boss nodes also (ChoiceScreenUtils:makeMapChoice branches for it)
 */

package swapthespire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;

import communicationmod.ChoiceScreenUtils;

import java.util.ArrayList;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import com.megacrit.cardcrawl.map.MapRoomNode;

public class CommunicationModMapChoicePatch {

    @SpirePatch(
            clz= ChoiceScreenUtils.class,
            paramtypez={int.class},
            method="makeMapChoice"
    )
    public static class ReplaceMakeMapChoiceLogic {
        public static SpireReturn Prefix(int choice_index) {

            // taken from ChoiceScreenUtils.makeMapChoice() [727]
            MapRoomNode currMapNode = AbstractDungeon.getCurrMapNode();
            /*
            if(currMapNode.y == 14 || (AbstractDungeon.id.equals(TheEnding.ID) && currMapNode.y == 2)) {
                if(choice == 0) {
                    DungeonMapPatch.doBossHover = true;
                    return;
                } else {
                    throw new IndexOutOfBoundsException("Only a boss node can be chosen here.");
                }
            }
            */
            ArrayList<MapRoomNode> nodeChoices = ChoiceScreenUtils.getMapScreenNodeChoices();
            MapRoomNode targetNode = nodeChoices.get(choice_index);

            // modified from MapRoomNode.class:update [219]
            // modifications: remove graphics/sounds, replace `this` with `targetNode`, etc.
            if (!AbstractDungeon.firstRoomChosen) {
                AbstractDungeon.setCurrMapNode(targetNode);
            } else {
                (AbstractDungeon.getCurrMapNode()).taken = true;
            } 
            MapEdge connectedEdge = AbstractDungeon.getCurrMapNode().getEdgeConnectedTo(targetNode);
            if (connectedEdge != null){
                connectedEdge.markAsTaken();
            }
            AbstractDungeon.nextRoom = targetNode;
            AbstractDungeon.pathX.add(Integer.valueOf(targetNode.x));
            AbstractDungeon.pathY.add(Integer.valueOf(targetNode.y));
            CardCrawlGame.metricData.path_taken.add(AbstractDungeon.nextRoom.getRoom().getMapSymbol());
            if (!AbstractDungeon.isDungeonBeaten) {
                // replacing the following line with the relevant parts of the call tree because otherwise it waits for the transition
                // AbstractDungeon.nextRoomTransitionStart();

                // following is from AbstractDungeon.nextRoomTransitionStart
                AbstractDungeon.overlayMenu.proceedButton.hide();
                if (ModHelper.isModEnabled("Terminal")) {
                    AbstractDungeon.player.decreaseMaxHealth(1); 
                }

                // and then, I'd have like to call the `AbstractDungeon.nextRoomTransition(null);` but it is not a static-method
                // so I will budge and go through the static method `AbstractDungeon.nextRoomTransitionStart()` because it seems
                // to set a very short fading timer in Settings.FAST_MODE (see AbstractDungeon.fadeOut() [3078])
                AbstractDungeon.nextRoomTransitionStart();
                
            }

            if ((AbstractDungeon.getCurrRoom()).phase.equals(AbstractRoom.RoomPhase.COMPLETE)) {
                boolean normalConnection = AbstractDungeon.getCurrMapNode().isConnectedTo(targetNode);
                boolean wingedConnection = AbstractDungeon.getCurrMapNode().wingedIsConnectedTo(targetNode);
                if (normalConnection || Settings.isDebug || wingedConnection) {
                    if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP) {
                        AbstractDungeon.dungeonMapScreen.clicked = false;
                        AbstractDungeon.dungeonMapScreen.clickTimer = 0.0F;
                        if (!normalConnection && wingedConnection && AbstractDungeon.player.hasRelic("WingedGreaves")) {
                            (AbstractDungeon.player.getRelic("WingedGreaves")).counter--;
                            if ((AbstractDungeon.player.getRelic("WingedGreaves")).counter <= 0) {
                                AbstractDungeon.player.getRelic("WingedGreaves").setCounter(-2); 
                            }
                        } 
                        if (targetNode.room instanceof com.megacrit.cardcrawl.rooms.EventRoom) {
                            CardCrawlGame.mysteryMachine++; 
                        }
                    } 
                } 
            }

            if (!AbstractDungeon.firstRoomChosen && targetNode.y == 0 && (AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMPLETE) {
                    
                if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.MAP && (CInputActionSet.select.isJustPressed() || AbstractDungeon.dungeonMapScreen.clicked)) {
                AbstractDungeon.dungeonMapScreen.clicked = false;
                AbstractDungeon.dungeonMapScreen.clickTimer = 0.0F;
                AbstractDungeon.dungeonMapScreen.dismissable = true;
                if (!AbstractDungeon.firstRoomChosen)
                    AbstractDungeon.firstRoomChosen = true; 
                } 
                //targetNode.highlighted = true;
            } 

            return SpireReturn.Return(null);
        }
    }
}