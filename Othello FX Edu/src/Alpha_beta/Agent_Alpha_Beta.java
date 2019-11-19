package Alpha_beta;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;

import main.UserSettings;

public class Agent_Alpha_Beta extends Agent{

	public Agent_Alpha_Beta(PlayerTurn playerTurn) {
		super(playerTurn);
	}
	
	public Agent_Alpha_Beta(String name) {
		super(name, PlayerTurn.PLAYER_ONE);
	}
	
	public Agent_Alpha_Beta() {
		super(PlayerTurn.PLAYER_ONE);
	}

	@Override
	public AgentMove getMove(GameBoardState gameState) {
		int waitTime = UserSettings.MIN_SEARCH_TIME; // 1.5 seconds
		ThreadManager.pause(TimeSpan.millis(waitTime)); // Pauses execution for the wait time to cause delay
		return Alpha_beta_move.getMove(gameState, playerTurn);
	}

}
