# multiAgents.py
# --------------
# Licensing Information:  You are free to use or extend these projects for
# educational purposes provided that (1) you do not distribute or publish
# solutions, (2) you retain this notice, and (3) you provide clear
# attribution to UC Berkeley, including a link to http://ai.berkeley.edu.
# 
# Attribution Information: The Pacman AI projects were developed at UC Berkeley.
# The core projects and autograders were primarily created by John DeNero
# (denero@cs.berkeley.edu) and Dan Klein (klein@cs.berkeley.edu).
# Student side autograding was added by Brad Miller, Nick Hay, and
# Pieter Abbeel (pabbeel@cs.berkeley.edu).


from util import manhattanDistance
from game import Directions
import random, util

from game import Agent
from pacman import GameState

class ReflexAgent(Agent):
    """
    A reflex agent chooses an action at each choice point by examining
    its alternatives via a state evaluation function.

    The code below is provided as a guide.  You are welcome to change
    it in any way you see fit, so long as you don't touch our method
    headers.
    """


    def getAction(self, gameState: GameState):
        """
        You do not need to change this method, but you're welcome to.

        getAction chooses among the best options according to the evaluation function.

        Just like in the previous project, getAction takes a GameState and returns
        some Directions.X for some X in the set {NORTH, SOUTH, WEST, EAST, STOP}
        """
        # Collect legal moves and successor states
        legalMoves = gameState.getLegalActions()

        # Choose one of the best actions
        scores = [self.evaluationFunction(gameState, action) for action in legalMoves]
        bestScore = max(scores)
        bestIndices = [index for index in range(len(scores)) if scores[index] == bestScore]
        chosenIndex = random.choice(bestIndices) # Pick randomly among the best

        "Add more of your code here if you want to"

        return legalMoves[chosenIndex]

    def evaluationFunction(self, currentGameState: GameState, action):
        """
        Design a better evaluation function here.

        The evaluation function takes in the current and proposed successor
        GameStates (pacman.py) and returns a number, where higher numbers are better.

        The code below extracts some useful information from the state, like the
        remaining food (newFood) and Pacman position after moving (newPos).
        newScaredTimes holds the number of moves that each ghost will remain
        scared because of Pacman having eaten a power pellet.

        Print out these variables to see what you're getting, then combine them
        to create a masterful evaluation function.
        """
        # Useful information you can extract from a GameState (pacman.py)
        successorGameState = currentGameState.generatePacmanSuccessor(action)
        newPos = successorGameState.getPacmanPosition()
        newFood = successorGameState.getFood()
        newGhostStates = successorGameState.getGhostStates()
        newScaredTimes = [ghostState.scaredTimer for ghostState in newGhostStates]  
        #newGhostStates[0].configuration.pos
        
        TotalMHD = 0
         #calculate the manhattan distance between pacman and all of the ghost 
        for ghost in newGhostStates:
            TotalMHD+=manhattanDistance(newPos, ghost.getPosition())
        #negate score to make pacman want to eat the ghost
        if newScaredTimes[0] > 0:
            #maybe make it cost more
             TotalMHD *= -1
        #if the ghost is 1 unit away from pacman in any direction, inflate score
        #maybe only consider the closest ghost
        #for ghost in newGhostStates:
            #if manhattanDistance(newPos, ghost.getPosition()) <= 5 and newScaredTimes[0] == 0:
                #TotalMHD*=5
        #either use length of list or manhattan distance
        closest_ghost = 10000
        closest_food = 10000
        for ghost in newGhostStates:
            closest_ghost = min(closest_ghost,manhattanDistance(newPos, ghost.getPosition()))
        for food in newFood.asList():
            closest_food =  min(closest_food, manhattanDistance(newPos, food))
        if closest_ghost <= 2:
            closest_ghost*5
        elif newScaredTimes[0] > 0:
            closest_ghost = 1/closest_ghost
        else:
            closest_ghost = 0
        food_left = len(newFood.asList())
        return successorGameState.getScore() + 1/closest_food + (1/(food_left**0)) 
        #stopping beause there is no food next to it 
        #add totalmhd back

def scoreEvaluationFunction(currentGameState: GameState):
    """
    This default evaluation function just returns the score of the state.
    The score is the same one displayed in the Pacman GUI.

    This evaluation function is meant for use with adversarial search agents
    (not reflex agents).
    """
    return currentGameState.getScore()

class MultiAgentSearchAgent(Agent):
    """
    This class provides some common elements to all of your
    multi-agent searchers.  Any methods defined here will be available
    to the MinimaxPacmanAgent, AlphaBetaPacmanAgent & ExpectimaxPacmanAgent.

    You *do not* need to make any changes here, but you can if you want to
    add functionality to all your adversarial search agents.  Please do not
    remove anything, however.

    Note: this is an abstract class: one that should not be instantiated.  It's
    only partially specified, and designed to be extended.  Agent (game.py)
    is another abstract class.
    """

    def __init__(self, evalFn = 'scoreEvaluationFunction', depth = '2'):
        self.index = 0 # Pacman is always agent index 0
        self.evaluationFunction = util.lookup(evalFn, globals())
        self.depth = int(depth)  

class MinimaxAgent(MultiAgentSearchAgent):
    """
    Your minimax agent (question 2)
    """
    def max_value(self, state, agentIndex, depth):
        node = float('-inf')
        if state.isLose() or state.isWin() or depth == self.depth:
            #print("Return Eval:", self.evaluationFunction(state))
            return self.evaluationFunction(state)
        for actions in state.getLegalActions(agentIndex):
            successor = state.generateSuccessor(agentIndex, actions)
            node = max(node, self.min_value(successor, 1, depth))
        #print("Return Node:", node)
        return node

    def min_value(self, state,agentIndex, depth):
        node = float('inf')
        #print(state.getLegalActions(agentIndex))
        for actions in state.getLegalActions(agentIndex):
            successor = state.generateSuccessor(agentIndex, actions)
            next_agent = (agentIndex+1)% state.getNumAgents()
            if next_agent == 0:
                node = min(node, self.max_value(successor, next_agent, depth + 1))
            else:
                node = min(node, self.min_value(successor, next_agent, depth))
        #print("Return Node:", node)
        return node
            #generate successors go to the next agent until you get to pacman
            #track depth



    def getAction(self, gameState: GameState):
        """
        Returns the minimax action from the current gameState using self.depth
        and self.evaluationFunction.

        Here are some method calls that might be useful when implementing minimax.

        gameState.getLegalActions(agentIndex):
        Returns a list of legal actions for an agent
        agentIndex=0 means Pacman, ghosts are >= 1

        gameState.generateSuccessor(agentIndex, action):
        Returns the successor game state after an agent takes an action

        gameState.getNumAgents():
        Returns the total number of agents in the game

        gameState.isWin():
        Returns whether or not the game state is a winning state

        gameState.isLose():
        Returns whether or not the game state is a losing state
        """

    
        take = None
        score = -float("inf")
        for action in gameState.getLegalActions():
            check_score = self.playgame(gameState.generateSuccessor(0, action), 1, 0)
            if score < check_score:
                score = check_score
                take = action
        return take

        util.raiseNotDefined()

    def minAgent(self, gameState, agentIndex, depth):
        value = float("inf")
        for action in gameState.getLegalActions(agentIndex): 
            
            if agentIndex != gameState.getNumAgents() - 1:
                value = min(value, self.playgame(gameState.generateSuccessor(agentIndex, action), agentIndex + 1, depth))
            else:
                value = min(value, self.playgame(gameState.generateSuccessor(agentIndex, action), 0, depth + 1))
        return value

    def maxAgent(self, gameState, depth):
        value= -float("inf")
        for action in gameState.getLegalActions(0):
            value = max(value, self.playgame(gameState.generateSuccessor(0, action), 1, depth)) 
        return value
    
    def playgame(self, gameState, agentIndex, depth):
        if gameState.isWin() or gameState.isLose() or depth == self.depth:
            return self.evaluationFunction(gameState)
        if agentIndex != 0:
            return self.minAgent(gameState, agentIndex, depth)
        return self.maxAgent(gameState, depth)
    
        # actions = 'North'
        # value = float('-inf')
        # for action in gameState.getLegalActions(0):
        #     node = self.min_value(gameState.generateSuccessor(0, action), 1, 0)
        #     #print("NODE:", node, "Value:", value, "Action:", action)
        #     if node > value:
        #         #print("check", node, value)
        #         value = node
        #         actions = action
        #         #print("Update:", actions)
        # #print("Return:", actions)
        # return actions

        util.raiseNotDefined()

class AlphaBetaAgent(MultiAgentSearchAgent):
    """
    Your minimax agent with alpha-beta pruning (question 3)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the minimax action using self.depth and self.evaluationFunction
        """
        "*** YOUR CODE HERE ***"
        def miniAgent(gameState, agentIndex, depth, alpha, beta):
            if gameState.isWin() or depth == 0 or gameState.isLose():
                return self.evaluationFunction(gameState)
   
            value = float("inf")
            for action in gameState.getLegalActions(agentIndex):
                if agentIndex != gameState.getNumAgents() - 1:
                    value=  min(value , miniAgent(gameState.generateSuccessor(agentIndex, action), agentIndex + 1, depth - 1, alpha, beta))
                else:
                    value =  min(value , maxiAgent(gameState.generateSuccessor(agentIndex, action), depth - 1, alpha, beta))
                if value < alpha:
                    return value
                beta = min(beta, value)
            return value
        
        def maxiAgent(gameState, depth, alpha, beta):
            if gameState.isWin() or depth == 0 or gameState.isLose():
                return self.evaluationFunction(gameState)
            value = -float("inf")
            for action in gameState.getLegalActions():
                value = max(value, miniAgent(gameState.generateSuccessor(0, action), 1, depth - 1, alpha, beta))
                if value > beta:
                    return value
                alpha = max(alpha, value)
            return value

        take = None
        beta = float("inf")
        alpha = -float("inf")
        score= -float("inf")

        for action in gameState.getLegalActions():
            if miniAgent(gameState.generateSuccessor(0, action), 1, (self.depth * gameState.getNumAgents() - 1), alpha, beta) > score:
                take = action
                score= miniAgent(gameState.generateSuccessor(0, action), 1, (self.depth * gameState.getNumAgents() - 1), alpha, beta)
                
            alpha = max(alpha, score)
            if score > beta:
                break
        return take
        util.raiseNotDefined()

class ExpectimaxAgent(MultiAgentSearchAgent):
    """
      Your expectimax agent (question 4)
    """

    def getAction(self, gameState: GameState):
        """
        Returns the expectimax action using self.depth and self.evaluationFunction

        All ghosts should be modeled as choosing uniformly at random from their
        legal moves.
        """
        "*** YOUR CODE HERE ***" 
        take = None
        score = -float("inf")

        for action in gameState.getLegalActions():
            if self.playgame(gameState.generateSuccessor(0, action), 1, 0) > score:
                score = self.playgame(gameState.generateSuccessor(0, action), 1, 0)
                take = action
        return take

        util.raiseNotDefined()

    def playgame(self, gameState, agentIndex, depth):
        if depth == self.depth or gameState.isWin() or gameState.isLose():
            return self.evaluationFunction(gameState)
        if agentIndex != 0:
            return self.expectimax(gameState, agentIndex, depth)
        return self.maxAgent(gameState, depth)
    
  
    def maxAgent(self, gameState, depth):
        value= -float("inf")
        for action in gameState.getLegalActions(0):
            value = max(value, self.playgame(gameState.generateSuccessor(0, action), 1, depth))
        return value
    
    def expectimax(self, gameState, agentIndex, depth):
        
        r_value = 0
        for action in gameState.getLegalActions(agentIndex):
            successorState = gameState.generateSuccessor(agentIndex, action)

            if agentIndex == gameState.getNumAgents() - 1:
                r_value += self.playgame(successorState, 0, depth + 1) / len(gameState.getLegalActions(agentIndex))
            else:
                r_value += self.playgame(successorState, agentIndex + 1, depth) / len(gameState.getLegalActions(agentIndex))
        
        return r_value

    
def betterEvaluationFunction(currentGameState: GameState):
    """
    Your extreme ghost-hunting, pellet-nabbing, food-gobbling, unstoppable
    evaluation function (question 5).

    DESCRIPTION: <keeing track of where food is so that pacman is motivated to eat more food and is rewarded with a hgher score in doing so and tracking food left so that pacman can eat all of the food>
    """
    "*** YOUR CODE HERE ***"
    food = currentGameState.getFood()
    pacman = currentGameState.getPacmanPosition()

    food_x_y= food.asList()
    numfood = currentGameState.getNumFood()

    if numfood > 0:
        min_food_distance = manhattanDistance(pacman, min(food_x_y, key=lambda food: manhattanDistance(pacman, food)))
    else: min_food_distance = 0

    return currentGameState.getScore() - min_food_distance - numfood
    util.raiseNotDefined()

# Abbreviation
better = betterEvaluationFunction
