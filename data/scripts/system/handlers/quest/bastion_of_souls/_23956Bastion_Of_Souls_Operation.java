/*
 * =====================================================================================*
 * This file is part of Aion-Unique (Aion-Unique Home Software Development)             *
 * Aion-Unique Development is a closed Aion Project that use Old Aion Project Base      *
 * Like Aion-Lightning, Aion-Engine, Aion-Core, Aion-Extreme, Aion-NextGen, ArchSoft,   *
 * Aion-Ger, U3J, Encom And other Aion project, All Credit Content                      *
 * That they make is belong to them/Copyright is belong to them. And All new Content    *
 * that Aion-Unique make the copyright is belong to Aion-Unique                         *
 * You may have agreement with Aion-Unique Development, before use this Engine/Source   *
 * You have agree with all of Term of Services agreement with Aion-Unique Development   *
 * =====================================================================================*
 */
package quest.bastion_of_souls;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/** Author Ghostfur & Unknown (Aion-Unique)
/****/

public class _23956Bastion_Of_Souls_Operation extends QuestHandler {

    private final static int questId = 23956;
	private final static int[] IDAb1EreBossLeader = {246544, 246545, 246546};
    public _23956Bastion_Of_Souls_Operation() {
        super(questId);
    }
	
	@Override
	public void register() {
		qe.registerOnEnterWorld(questId);
		qe.registerQuestNpc(806591).addOnTalkEvent(questId);
		for (int mob: IDAb1EreBossLeader) {
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("IDAB1_ERE_Q13956_A_302340000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDAB1_ERE_Q13956_B_302340000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDAB1_ERE_Q13956_C_302340000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDAB1_ERE_Q13956_D_302340000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 806591) {
                if (env.getDialog() == QuestDialog.START_DIALOG) {
                    return sendQuestDialog(env, 10002);
				} else if (env.getDialog() == QuestDialog.SELECT_REWARD) {
					return sendQuestDialog(env, 5);
				} else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (player.getWorldId() == 302340000) { //Bastion Of Souls 5.5
            if (qs == null || qs.canRepeat()) {
                env.setQuestId(questId);
                if (QuestService.startQuest(env)) {
					return true;
				}
            }
        }
        return false;
    }
	
	@Override
    public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
        final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("IDAB1_ERE_Q13956_A_302340000")) {
				if (var == 0) {
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("IDAB1_ERE_Q13956_B_302340000")) {
				if (var == 2) {
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("IDAB1_ERE_Q13956_C_302340000")) {
				if (var == 3) {
					changeQuestStep(env, 3, 4, false);
					return true;
				}
			} else if (zoneName == ZoneName.get("IDAB1_ERE_Q13956_D_302340000")) {
				if (var == 4) {
					changeQuestStep(env, 4, 5, true);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env) {
		final Player player = env.getPlayer();
        final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs != null && qs.getStatus() == QuestStatus.START) {
			int var = qs.getQuestVarById(0);
			if (var == 1) {
				if (env.getTargetId() == 246544 || //IDAb1_Ere_Boss_Leader_Hard_75_Ah.
				    env.getTargetId() == 246545 || //IDAb1_Ere_Boss_Leader_Normal_75_Ah.
					env.getTargetId() == 246546) { //IDAb1_Ere_Boss_Leader_Easy_75_Ah.
					changeQuestStep(env, 1, 2, false);
					return true;
				}
			}
		}
		return false;
	}
}