/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.morheim;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;


/**
 * @author Cheatkiller
 *
 */
public class _2477ADishForDukar extends QuestHandler {

	private final static int questId = 2477;
	public _2477ADishForDukar() {
		super(questId);
	}

	public void register() {
		qe.registerQuestNpc(204355).addOnQuestStart(questId);
		qe.registerQuestNpc(204355).addOnTalkEvent(questId);
		qe.registerQuestNpc(204100).addOnTalkEvent(questId);
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (qs == null || qs.getStatus() == QuestStatus.NONE) {
			if (targetId == 204355) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1011);
				}
				else if(dialog == QuestDialog.ACCEPT_QUEST) {
					QuestService.startQuest(env);
					changeQuestStep(env, 0, 1, false);
					return sendQuestDialog(env, 1003);
				}
				else {
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 204355) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1352);
				}
				else if(dialog == QuestDialog.CHECK_COLLECTED_ITEMS) {
				  return checkItemExistence(env, 1, 2, false, 182204196, 5, true, 10000, 10001, 182204234, 1);
				}
				else if(dialog == QuestDialog.STEP_TO_2) 
				  return defaultCloseDialog(env, 1, 2);
				}
			else if (targetId == 204100) { 
				if (dialog == QuestDialog.START_DIALOG) {
					return sendQuestDialog(env, 1693);
				}
			  else if (dialog == QuestDialog.SET_REWARD) {
			  	removeQuestItem(env, 182204234, 1);
			  	giveQuestItem(env, 182204197, 1);
					return defaultCloseDialog(env, 2, 3, true, false);
				}
			}
		}
		else if (qs == null || qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 204355) {
				if (dialog == QuestDialog.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					removeQuestItem(env, 182204197, 1);
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}