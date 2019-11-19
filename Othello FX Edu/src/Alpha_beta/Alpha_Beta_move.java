package Alpha_beta;

import java.util.List;

import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;

public class Alpha_Beta_move {
	
	private int beta = Integer.MAX_VALUE, alpha = Integer.MIN_VALUE;
	
	
	

	
	/**
	 * Here is where our Alpha Beta pruning will be implemented
	 * @param gameState
	 * @param playerTurn
	 * @return
	 */
	public static MoveWrapper getMove(GameBoardState currentState, PlayerTurn turn) {
		//Fetch all the possible moves for the current player given the current state
		
		
		
		return alphaBetaPruning(currentState, turn);
	}

	private static MoveWrapper alphaBetaPruning(GameBoardState currentState, PlayerTurn turn) {
		List<ObjectiveWrapper> agentMoves = getAvailableMoves(currentState, turn);
		
		
		
		return null;
	}
	
	private static int evaluate(GameBoardState currentState, PlayerTurn turn, MoveWrapper move) {
		
		
		
		
		return 0;
	}
	
	
	
	
	
	/**
	 * Returns a List containing all the possible moves a given player can make given a state
	 * Uses the method from the given framework 
	 * @param currentState
	 * @param turn
	 * @return
	 */
	private static List<ObjectiveWrapper> getAvailableMoves(GameBoardState currentState, PlayerTurn turn) {
		return AgentController.getAvailableMoves(currentState, turn);
	}
	
}
