package Alpha_beta;

import java.util.List;

import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.utilities.GameTreeUtility;

public class Alpha_Beta_move {
	
	private static int depth = 0;
	private static int pruned = 0;
	private static int NodesSearched = 0;
	

	
	/**
	 * Here is where our Alpha Beta pruning will be implemented
	 * @param gameState
	 * @param playerTurn
	 * @return
	 */
	public static MoveWrapper getMove(GameBoardState currentState, PlayerTurn turn, int ply) {
		//Fetch all the possible moves for the current player given the current state	
		depth = ply;
		pruned = 0;
		NodesSearched = 0;
		return alphaBetaPruning(currentState, turn, ply);
	}

	private static MoveWrapper alphaBetaPruning(GameBoardState currentState, PlayerTurn turn, int ply) {
		List<ObjectiveWrapper> agentMoves = getAvailableMoves(currentState, turn);
		ObjectiveWrapper returnMove;
		if (!agentMoves.isEmpty()) {
			returnMove = agentMoves.get(0);
		}else{
			return new MoveWrapper(null);
		}
		double bestScore = Double.MIN_VALUE;

		for (ObjectiveWrapper objWrp : agentMoves) {
			NodesSearched++;
			//possible fuck up later
			GameBoardState tempState = AgentController.getNewState(currentState, objWrp);
			//Ah recursion goes here
			double score = minScoreAlphaBeta(tempState,getCounterPlayer(turn), ply, Double.MAX_VALUE, Double.MIN_VALUE);
			if (score > bestScore) {
				bestScore = score;
				returnMove = objWrp;
			}
		}
		System.out.println("Search depth: " + depth + "\n" + "Number of pruned trees: " + pruned + "\n" + "Number of searched nodes: " + NodesSearched + "\n\n");
		return new MoveWrapper(returnMove);
	}
	
	private static double minScoreAlphaBeta(GameBoardState tempState, PlayerTurn player, int ply, double alpha, double beta) {
		if(ply == 0){
			return heuristicEvaluation(tempState);
		}
		double bestScore = Double.MAX_VALUE;
		boolean oncePerLoop = true;
		for (ObjectiveWrapper objWrp : getAvailableMoves(tempState, player) ) {
			NodesSearched++;
			if (oncePerLoop) {
				oncePerLoop = false;
			}
			GameBoardState minState = AgentController.getNewState(tempState, objWrp);
			double score = maxScoreAlphaBeta(minState, getCounterPlayer(player), ply-1, alpha, beta);
			if (score < bestScore) {
				bestScore = score;
			}
			if (bestScore <= alpha) {
				pruned++;
				return bestScore;
			}
			beta = Math.min(beta, bestScore);
		}
		return bestScore;	
	}

	private static double maxScoreAlphaBeta(GameBoardState tempState, PlayerTurn player, int ply, double alpha, double beta) {

		if(ply == 0){
			return heuristicEvaluation(tempState);
		}
		double bestScore = Double.MIN_VALUE;
		boolean oncePerLoop = true;
		for (ObjectiveWrapper objWrp : getAvailableMoves(tempState, player) ) {
			NodesSearched++;
			if (oncePerLoop) {
				oncePerLoop = false;
			}
			GameBoardState maxState = AgentController.getNewState(tempState, objWrp);
			double score = minScoreAlphaBeta(maxState, getCounterPlayer(player), ply-1, alpha, beta);
			if (score > bestScore) {
				bestScore = score;
			}
			if (bestScore >= beta) {
				pruned++;
				return bestScore;
			}
			alpha = Math.max(alpha, bestScore);
		}
		return bestScore;
	}

	private static PlayerTurn getCounterPlayer(PlayerTurn player) {
		return GameTreeUtility.getCounterPlayer(player);
	}

	/**
	 * Returns a value that determines the value of a given gamestate
	 * 
	 * @param currentState
	 * @return double evaluation
	 */
	private static double heuristicEvaluation(GameBoardState currentState) {
		return AgentController.getDynamicHeuristic(currentState);
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
