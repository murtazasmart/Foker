package Controller;

import Model.Card;
import Model.Player;

/**
 *
 * @author asus i3
 */
public class Referee {

    Card c;
    String [] cardpackc;
    String[][] playerCarray;
    int playerscount;

    //INITIATES THE CARD VALUES PLAYER OBJECTS ETC. ACCORDING TO NUMBER OF PLAYERS
    public Player[] startGame(Player[] players){
        playerscount = players.length;
        System.out.println("Game Started (refereee)");
        c = new Card();
        cardpackc = c.shuffle();
        System.out.println("cards shuffled (refereee)");
        System.out.println("cards=");
        c.pushCard(cardpackc);
        System.out.println("pushed");

        playerCarray = distributeCard(playerscount);

        String[] usernames = storeUsernames(players);

        for(int i =0; i<playerscount;i++){
            players[i].getObjectReady(playerscount);
            players[i].setNumberofplayers(playerscount);
            players[i].setCardHand(playerCarray);
            players[i].setAllUsernames(usernames);
        }

        playerCarray = null;

        players = calculateScoreRoundA(players);

        return players;
    }

    public String[] storeUsernames(Player[] players){
        String[] usernames = new String[players.length] ;
        for(int i =0; i< playerscount;i++){
            usernames[i] = players[i].getUsername();
        }
        return usernames;
    }



    public String[][] distributeCard(int playerscount){
        playerCarray = new String [playerscount][5];
        if (playerscount*5<=52) {
            for (int i=0;i<5;i++) {
                for(int j=0;j<playerscount;j++){
                    playerCarray[j][i]=c.pullcard();
                }
            }
        }
        return playerCarray;
    }

    public String swapPlayerCards(String currentCard){
        c.pushCard(currentCard);
        return c.pullcard();
    }

    public Player[] cardExchange(Player[] players){
        for(int i = 0; i< playerscount;i++){
            for(int j =0;j<5;j++){
                if(players[i].getSwapCards()[j] ==true){
                    String currentCard = players[i].getIndividualCardHand(i,j);
                    currentCard = swapPlayerCards(currentCard);
                    players[i].setIndividualCardHand(i, j, currentCard);
                }
            }
        }
        //NOTE INDIVDUAL GET HAND ON EACH PLAYER ISN'T ACCURATE
        return players;
    }

    public Player[] calculateScoreRoundA(Player[] players){
        int valueScore=0, typeScore=0, totalScoreNew=0, totalScoreOld, risk=0, alpha;
        String [][] playerCardHand;
        int[] allScores = new int[playerscount];
        for(int i = 0;i<playerscount;i++){
            totalScoreOld = players[i].getScore();
            playerCardHand = players[i].getCardHand();
            typeScore = getTypeScore(i, playerCardHand);
            valueScore = getValueScore(i, playerCardHand);
            risk = getRiskValue(i, playerCardHand);
            alpha = valueScore + typeScore;
            totalScoreNew = typeScore + valueScore + totalScoreOld;
            players[i].setRisk(risk);
            players[i].setScore(totalScoreNew);
            players[i].setAlpha(alpha);
            allScores[i] = totalScoreNew;
        }
        for(int i = 0;i<playerscount;i++){
            players[i].setAllScores(allScores);
        }

        return players;
    }

    public Player[] calculateScoreRoundB(Player[] players){
        int valueScore=0, typeScore=0, beta=0, alpha=0, sigma=1, totalScore, newRisk =0 ;
        String [][] playerCardHand;
        int[] allScores = new int[playerscount];
        for(int i = 0;i<playerscount;i++){
            totalScore = players[i].getScore();
            alpha = players[i].getAlpha();
            playerCardHand = players[i].getCardHand();
            typeScore = getTypeScore(i, playerCardHand);
            valueScore = getValueScore(i, playerCardHand);
//            newRisk = getRiskValue(i, playerCardHand);
            beta = valueScore + typeScore;
            if(alpha<beta){
                sigma = 1;
                totalScore = (beta-alpha)+sigma*players[i].getRisk();
            }else{
                sigma = -1;
                totalScore = (alpha-beta)+sigma*players[i].getRisk();
            }
            players[i].setScore(totalScore);
            allScores[i] = totalScore;
        }
        for(int i = 0;i<playerscount;i++){
            players[i].setAllScores(allScores);
        }

        return players;
    }

    public Player[] updatePlayerToBeKicked(Player[] players){
        int lowestScore = 999, lowestScorePlayerId = 0;
        for(int i = 0;i<players.length;i++){
            if(players[i].getScore()<lowestScore ){
                lowestScore = players[i].getScore();
                lowestScorePlayerId = i;
            }
        }
        players[lowestScorePlayerId].setKicked(true);
        return players;
    }

    public Player[] updatePointsDonation(Player[] players){
        for(int i = 0;i<playerscount;i++){
            if(players[i].isKicked() == true ){
                players[players[i].getPlayerIdToDonatePoints()].updateDonationScore((players[i].getScore()/4));
            }
        }
        return players;
    }

    public int getTypeScore(int playerId, String[][] playerCardHand){
        String type = null;
        int typeScore = 0;
        for(int i=0;i<5;i++){
            type = playerCardHand[playerId][i].substring(0,playerCardHand[playerId][i].indexOf("-"));
            System.out.println("user "+ playerId+" card type= "+type);
            switch(type){
                case "s":
                    typeScore+=5;
                    break;
                case "d":
                    typeScore+=4;
                    break;
                case "c":
                    typeScore+=3;
                    break;
                case "h":
                    typeScore+=2;
                    break;
            }
        }
        String firstcard = playerCardHand[playerId][0].substring(0, 1);
        String secondcard = playerCardHand[playerId][1].substring(0, 1);

        if ((firstcard.equals("d") & secondcard.equals("h"))) {
            typeScore += 5;

        } else if ((firstcard.equals("d") & secondcard.equals("s"))) {
            typeScore += 5;
        } else if ((firstcard.equals("h") & secondcard.equals("s"))) {
            typeScore += 5;
        } else if ((firstcard.equals("s") & secondcard.equals("h"))) {
            typeScore += 5;
        } else if ((firstcard.equals("s") & secondcard.equals("h"))) {
            typeScore += 5;
        } else if ((firstcard.equals("h") & secondcard.equals("d"))) {
            typeScore += 5;
        } else if ((firstcard.equals("s") & secondcard.equals("d"))) {
            typeScore += 5;
        } else if ((firstcard.equals("h") & secondcard.equals("c "))) {
            typeScore += 5;
        } else {
            typeScore += 0;
        }


        return typeScore;
    }

    public int getValueScore(int playerId, String[][] playerCardHand){
        String value=null;
        int valueScore = 0;
        for(int i=0;i<5;i++){
            value=playerCardHand[playerId][i].substring(playerCardHand[playerId][i].indexOf("-")+1);
            System.out.println("user "+ playerId+ " card value= "+value);
            switch(value){
                case "A":
                    valueScore+=15;
                    break;
                case "K":
                    valueScore+=14;
                    break;
                case "Q":
                    valueScore+=13;
                    break;
                case "J":
                    valueScore+=12;
                    break;
                case "10":
                    valueScore+=11;
                    break;
                case "9":
                    valueScore+=10;
                    break;
                case "8":
                    valueScore+=9;
                    break;
                case "7":
                    valueScore+=8;
                    break;
                case "6":
                    valueScore+=7;
                    break;
                case "5":
                    valueScore+=6;
                    break;
                case "4":
                    valueScore+=5;
                    break;
                case "3":
                    valueScore+=4;
                    break;
                case "2":
                    valueScore+=3;
                    break;
            }
            if (value.equals("A") & value.equals("K") & value.equals("Q") & value.equals("J")) {
                valueScore += 8;
            } else if (value.equals("10") & value.equals("9") & value.equals("8") & value.equals("8")) {
                valueScore += 5;
            } else if (value.equals("10") & value.equals("9") & value.equals("8")) {
                valueScore += 6;

            } else if (value.equals("A") | value.equals("K") | value.equals("Q") | value.equals("J")) {
                valueScore += 4;
            } else if (value.equals("10") | value.equals("9") | value.equals("8")) {
                valueScore += 2;
            }
        }
        return valueScore;
    }

    //Risk calculation before change hand
    public int getRiskValue(int playerId, String[][] playerCardHand) {
        String type = null;
        String value=null;
        int riskValue = 0;
            for (int i = 0; i < 5; i++) {
                type = playerCardHand[playerId][i].substring(0, playerCardHand[playerId][i].indexOf("-"));
                value=playerCardHand[playerId][i].substring(0, playerCardHand[playerId][i].indexOf("-")+1);

                switch (type) {

                    case "s":
                        riskValue += 4;
                        break;

                    case "d":
                        riskValue += 3;
                        break;

                    case "c":
                        riskValue += 2;
                        break;

                    case "h":
                        riskValue += 1;
                        break;
                }
            }
            System.out.println("The Risk of user" +playerId+ " for Hand: " + riskValue);
        return riskValue;
    }
}
