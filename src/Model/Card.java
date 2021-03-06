package Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;

import static java.util.logging.Logger.getLogger;

/**
 * Created by asus on 6/18/2017.
 */
public class Card {
    //public String pack []={"sA", "s2", "s3","s4","s5","s6","s7","s8", "s9","s10", "sJ", "sQ", "sK",/**/"hA", "h2", "h3","h4","h5","h6","h7","h8", "h9","h10", "hJ", "hQ", "hK",/**/"dA", "d2", "d3","d4","d5","d6","d7","d8", "d9","d10", "dJ", "dQ", "dK",/**/"cA", "c2", "c3","c4","c5","c6","c7","c8", "c9","c10", "cJ", "cQ", "cK",};
    // public String pack []={"s-A", "s-2", "s-3","s-4","s-5","s-6","s-7","s-8", "s-9","s-10", "s-J", "s-Q", "s-K",/**/"h-A", "h-2", "h-3","h-4","h-5","h-6","h-7","h-8", "h-9","h-10", "h-J", "h-Q", "h-K",/**/"d-A", "d-2", "d-3","d-4","d-5","d-6","d-7","d-8", "d-9","d-10", "d-J", "d-Q", "d-K",/**/"c-A", "c-2", "c-3","c-4","c-5","c-6","c-7","c-8", "c-9","c-10", "c-J", "c-Q", "c-K",};

    public String pack[]={"s-A","d-A","c-A","h-A","s-K","d-K","c-K","h-K","s-Q","d-Q","c-Q","h-Q","s-J","d-J","c-J","h-J",
            "s-10", "d-10","c-10","h-10","s-9","d-9","c-9", "h-9","s-8","d-8", "c-8","h-8","s-7","d-7","c-7","h-7",
            "s-6","d-6","c-6","h-6","s-5","d-5","c-5","h-5","s-4","d-4","c-4","h-4","s-3","d-3","c-4","h-3",
            "s-2","d-2","c-2","h-2",};
    ArrayBlockingQueue<String> spack= new ArrayBlockingQueue(52);


    public String[] shuffle()
    {
        int cardnumber[]= new int[52];
        String shfpack[]= new String[52];


        for(int x=0;x<52;x++){
            int cardid=0;
            boolean randno=false;
            while(randno==false){
                cardid= (int) (Math.random()*100);
                if (cardid<52) {
                    if (pack[cardid] != "done") {
                        randno = true;
                    }
                }
            }

            boolean isDone=false;
            int y=0;
            while (y<52){

                if (pack[y]=="done"){
                    isDone=true;
                }else{
                    isDone=false;
                    break;
                }
                y++;
            }
            if (isDone==false) {
                if (pack[cardid]!="done"){
                    shfpack[x] = pack[cardid];
                    pack[cardid] = "done";
                }

            }
        }
        for (int xx=0; xx<52;xx++){

            System.out.print(pack[xx]+",");
        }
        return shfpack;
    }

    public void pushCard(String [] shfpack) {

        for (int x=0;x<52;x++){
            try {
                spack.put(shfpack[x]);
            } catch (InterruptedException ex) {
                getLogger(Card.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    public void pushCard(String onecard){
        try {
            spack.put(onecard);
        } catch (InterruptedException ex) {
            getLogger(Card.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String pullcard(){
        return spack.poll();
    }

    public String getCardNumber (String Ecard){
        String[] parts = Ecard.split("-");
        String part2 = parts[1];
        return part2;
    }

}
