package quest.altgard;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog; // Заменён импорт
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;

public class _24016AStrangeNewThreat extends QuestHandler {

    private final static int questId = 24016;
    private final static int[] npcs = {203557, 700140};

    public _24016AStrangeNewThreat() {
        super(questId);
    }

    @Override
    public void register() {
        qe.registerOnEnterZoneMissionEnd(questId);
        qe.registerOnLevelUp(questId);
        qe.registerOnDie(questId);
        qe.registerOnEnterWorld(questId);
        for (int npc : npcs) {
            qe.registerQuestNpc(npc).addOnTalkEvent(questId);
        }
        qe.registerQuestNpc(233876).addOnKillEvent(questId);
    }

    @Override
    public boolean onDialogEvent(QuestEnv env) {
        Player player = env.getPlayer();
        int targetId = env.getTargetId();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        int var = qs.getQuestVarById(0);

        if (qs.getStatus() == QuestStatus.START) {
            switch (targetId) {
                case 203557: { // Suthran
                    // Было: DialogAction.QUEST_SELECT  -> Стало: QuestDialog.START_DIALOG
                    if (env.getDialog() == QuestDialog.START_DIALOG && var == 0) {
                        return sendQuestDialog(env, 1011);
                    // Было: DialogAction.SETPRO1 -> Стало: QuestDialog.STEP_TO_1 (или STEP_TO_2, см. ниже)
                    } else if (env.getDialog() == QuestDialog.STEP_TO_1) {
                        TeleportService2.teleportTo(player, 220030000, 2453.1934f, 2555.148f, 316.267f, (byte) 60, TeleportAnimation.BEAM_ANIMATION);
                        changeQuestStep(env, 0, 1, false); // 1
                        return closeDialogWindow(env);
                    } else if (env.getDialogId() == 1013) {
                        playQuestMovie(env, 66);
                        return sendQuestDialog(env, 1013);
                    }
                    break;
                }
                case 700140: { // Gate Guardian Stone
                    if (var == 2) {
                        // Было: DialogAction.USE_OBJECT -> Стало: QuestDialog.USE_OBJECT (если есть) или START_DIALOG
                        if (env.getDialog() == QuestDialog.USE_OBJECT) {
                            QuestService.addNewSpawn(320030000, player.getInstanceId(), 233876, 260.12f, 234.93f, 216.00f, (byte) 90);
                            return useQuestObject(env, 2, 3, false, false); // 3
                        }
                    } else if (var == 4) {
                        if (env.getDialog() == QuestDialog.USE_OBJECT) {
                            playQuestMovie(env, 154);
                            changeQuestStep(env, 4, 4, true); // reward
                            TeleportService2.teleportTo(env.getPlayer(), 220030000, 1683, 1753, 260, (byte) 60, TeleportAnimation.BEAM_ANIMATION);
                            return true;
                        }
                    }
                }
            }
        } else if (qs.getStatus() == QuestStatus.REWARD) {
            if (targetId == 203557) { // Suthran
                // Здесь можно оставить как есть, если sendQuestEndDialog обрабатывается хендлером
                if (env.getDialog() == QuestDialog.USE_OBJECT) {
                    return sendQuestDialog(env, 1352);
                } else {
                    return sendQuestEndDialog(env);
                }
            }
        }
        return false;
    }

    @Override
    public boolean onEnterWorldEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs == null) {
            return false;
        }
        if (qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVars().getQuestVars();
            if (var >= 2 && player.getWorldId() != 320030000) {
                changeQuestStep(env, var, 1, false);
                return true;
            } else if (var == 1 && player.getWorldId() == 320030000) {
                changeQuestStep(env, 1, 2, false); // 2
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKillEvent(QuestEnv env) {
        return defaultOnKillEvent(env, 233876, 3, 4); // 4
    }

    @Override
    public boolean onDieEvent(QuestEnv env) {
        Player player = env.getPlayer();
        QuestState qs = player.getQuestStateList().getQuestState(questId);
        if (qs != null && qs.getStatus() == QuestStatus.START) {
            int var = qs.getQuestVars().getQuestVars();
            if (var >= 2) {
                qs.setQuestVar(1);
                updateQuestStatus(env);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onZoneMissionEndEvent(QuestEnv env) {
        return defaultOnZoneMissionEndEvent(env);
    }

    @Override
    public boolean onLvlUpEvent(QuestEnv env) {
        int[] altgardQuests = {24011, 24012, 24013, 24014, 24015};
        return defaultOnLvlUpEvent(env, altgardQuests, true);
    }
}