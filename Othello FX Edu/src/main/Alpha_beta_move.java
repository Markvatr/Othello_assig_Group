package main;

import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;

public class Alpha_beta_move {
	
	public static MoveWrapper getMove(GameBoardState gameState, PlayerTurn playerTurn) {
		
		
		return AgentController.getExampleMove(gameState, playerTurn);
	}
	
}
