package Alpha_beta;

import java.util.List;

import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.utilities.GameTreeUtility;

public class Alpha_Beta_move {
	/**
	 * Final variables that define how long a move may take/when to stop
	 */
	private static final long ABORT_TIME_THRESHOLD = 100;
	private static final long TIMEOUT = 5000;
	
	/**
	 * Variables used for tracking
	 * Maxdepth defines an arbitrary depth that we are trying to reach
	 * startTime defines when we started the current move
	 * bestMove stores the temporary best move that the agent can make
	 */
	private static int Maxdepth = 0;
	private static long startTime = 0;
	private static ObjectiveWrapper bestMove = null;
	/**
	 * Output variables
	 */
	private static int pruned = 0;
	private static int nodesSearched = 0;
	private static int depthReached = 0;
	private static long timeTaken = 0;
	

	
	/**
	 * Start position for our alpha beta pruning
	 * @param gameState
	 * @param playerTurn
	 * @return
	 */
	public static MoveWrapper getMove(GameBoardState state, PlayerTurn player, int depth) {
		//Start by setting/resetting our tracking vars
		startTime = System.currentTimeMillis();
		Maxdepth = depth;
		pruned = 0;
		nodesSearched = 0;

		//Can we make a move? if we can't then we just pass to the other player
		if (getAvailableMoves(state, player).isEmpty()) {
			return new MoveWrapper(null);
		}
		//Try finding the best move from our starting state
		maxScoreAlphaBeta(state, player, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
		System.out.println("\n" +
			"Move reached after : " + timeTaken + "ms\n" +
			"Search depth    	: " + depthReached +"\n" +
			"Nodes reached   	: " + nodesSearched + "\n" +
			"Branches pruned 	: " + pruned);
		return new MoveWrapper(bestMove);
	}

	/**
	 * Maxima score (our node)
	 * @param state the current boardstate
	 * @param player the current players turn
	 * @param alpha our alpha value
	 * @param beta our beta value
	 * @param depth the current depth of search
	 * @return the Worth of this node
	 */
	private static double maxScoreAlphaBeta(GameBoardState state, PlayerTurn player, double alpha, double beta, int depth) {
		//Are we allowed to continue searching?
		if(depth == Maxdepth || isTerminal(state, player) || !timeLeft()){
			return heuristicEvaluation(state);
		}
		//Ternary operator for output variable
		depthReached = ( (depth > depthReached) ? depth : depthReached );

		List<ObjectiveWrapper> moves = getAvailableMoves(state, player);

		//Do we have a legal move?
		if (moves.isEmpty()) {
			//If we don't then we continue the pruning but from the oponents perspective
			return minScoreAlphaBeta(state, getCounterPlayer(player), alpha, beta, depth + 1);
		}

		//Temporary storage for our best move
		ObjectiveWrapper localBestMove = null;

		double bestResult = Double.NEGATIVE_INFINITY;

		//For each move we can make
		for (ObjectiveWrapper coord : moves) {

			nodesSearched++;

			//Get the boardstate after we make said move
			GameBoardState newState = AgentController.getNewState(state, coord);

			//Calculate alpha based on recursion 
			alpha = minScoreAlphaBeta(newState, getCounterPlayer(player), alpha, beta, depth + 1);

			//If our new alpha is higher than our previous neigbour then overwrite it
			if (alpha > bestResult) {
				localBestMove = coord;
				bestResult = alpha;
			}
			//If our alpha is higher than our beta value then prune the branch
			if (alpha >= beta) {
				pruned++;
				break;
			}
		}

		bestMove = localBestMove;
		return alpha;
	}
	
	/**
	 * minimum score (not our node)
	 * @param state
	 * @param player
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	private static double minScoreAlphaBeta(GameBoardState state, PlayerTurn player, double alpha, double beta, int depth) {
		if(depth == Maxdepth || isTerminal(state, player) || !timeLeft()){
			return heuristicEvaluation(state);
		}
		//Ternary operator for output variable
		depthReached = ( (depth > depthReached) ? depth : depthReached );

		List<ObjectiveWrapper> moves = getAvailableMoves(state, player);

		//Do we have a legal move?
		if (moves.isEmpty()) {
			return maxScoreAlphaBeta(state, getCounterPlayer(player), alpha, beta, depth + 1);
		}

		//For each move we can make
		for (ObjectiveWrapper coords : moves) {
			nodesSearched++;

			//Get the boardstate after we make said move
			GameBoardState newState = AgentController.getNewState(state, coords);

			//Find the smalest value of the boardstates branch values
			beta = Math.min(beta, maxScoreAlphaBeta(newState, player, alpha, beta, depth + 1) );

			//If our beta value is smaler that our current alpha value then prune the branch
			if (beta <= alpha) {
				break;
			}
		}
		return beta;

	}
	/**
	 * Do we still have time to compute?
	 * @return boolean
	 */
	private static boolean timeLeft() {
		if ( TIMEOUT - (System.currentTimeMillis() - startTime) <= ABORT_TIME_THRESHOLD ) {
			timeTaken = (System.currentTimeMillis() - startTime);
			return false;
		}
		return true;
	}

	private static PlayerTurn getCounterPlayer(PlayerTurn player) {
		return GameTreeUtility.getCounterPlayer(player);
	}

	/**
	 * Returns the value of a given gamestate
	 * Uses the method from the given framework
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
	 * @return List<ObjectiveWrapper> moves
	 */
	private static List<ObjectiveWrapper> getAvailableMoves(GameBoardState currentState, PlayerTurn turn) {
		return AgentController.getAvailableMoves(currentState, turn);
	}
	/**
	 * Are we at a terminal node?
	 * Uses the method from the given framework 
	 * @param state
	 * @param player
	 * @return boolean
	 */
	private static boolean isTerminal(GameBoardState state, PlayerTurn player){
		return AgentController.isTerminal(state, player);
	}
	
}
