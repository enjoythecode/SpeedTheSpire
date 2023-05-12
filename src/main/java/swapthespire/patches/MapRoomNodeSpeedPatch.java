/*
 * We want to replace how CommunicationMod handles ChoiceScreenUtils:makeMapChoice(int choice_index) because it adds too much latency
 * 
 * TODO: Replicate the logic for the boss nodes also (ChoiceScreenUtils:makeMapChoice branches for it)
 */

package swapthespire.patches;

import basemod.ReflectionHacks;

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

public class MapRoomNodeSpeedPatch {

    @SpirePatch(
            clz= MapRoomNode.class,
            method="update"
    )
    public static class SpeedUpMapUpdateLogicTime {
        public static SpireReturn Prefix(MapRoomNode _instance) {

            if((float)ReflectionHacks.getPrivate(_instance, MapRoomNode.class, "animWaitTimer") > 0.0F){
                ReflectionHacks.setPrivate(_instance, MapRoomNode.class, "animWaitTimer", 0.0001F);
            }

            return SpireReturn.Continue();
        }
    }
}