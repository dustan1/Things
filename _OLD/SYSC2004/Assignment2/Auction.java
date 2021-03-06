import java.util.*;

/**
 * A simple model of an auction.
 * The auction maintains a list of lots of arbitrary length.
 *
 * @author David J. Barnes and Michael Kolling.
 * @version 2006.03.30
 *
 * @author (of AuctionSkeleton) Lynn Marshall
 * @version 2.0
 * 
 * @author Dorian Wang
 * @version 2.1, 21st May, 2016
 * 
 */
public class Auction
{
    /** The list of Lots in this auction. */
    private ArrayList<Lot> lots;

    /** 
     * The number that will be given to the next lot entered
     * into this auction.  Every lot gets a new number, even if some lots have
     * been removed.  (For example, if lot number 3 has been deleted, we don't
     * reuse it.)
     */
    private int nextLotNumber;
    private boolean isOpen;


    /**
     * Create a new auction.
     */
    public Auction()
    {
        lots = new ArrayList<Lot>();
        nextLotNumber = 1;
        isOpen = false;
        
    }
    
    /**
     * Add a second constructor here.  This constructor takes
     * an Auction as a parameter.  Provided the auction parameter
     * is closed, the constructor creates a new auction containing
     * the unsold lots of the closed auction.  If the auction parameter
     * is still open or null, this constructor behaves like the
     * default constructor.
     * 
     * Create a new auction from an old closed one.
     * 
     * @param oldAuction an old 
     */
    public Auction(Auction oldAuction)
    {
        if (oldAuction == null || oldAuction.isOpen()){
            lots = new ArrayList<Lot>();
            nextLotNumber = 1;
        }
        else
        {
            lots = oldAuction.getNoBids();
            nextLotNumber = oldAuction.nextLot();
       }
       isOpen = false;
    }

    /**
     * Enter a new lot into the auction.  Returns false if the
     * auction is not open or if the description is null.
     *
     * @param description A description of the lot.
     * 
     * @return true if the auction is open and the description is valid, false otherwise
     */
    public boolean enterLot(String description)
    {
        if (isOpen == false || description == null){
            return false;
        }
        
        lots.add(new Lot(nextLotNumber, description));
        nextLotNumber++;
        return true;
    }

    /**
     * Show the full list of lots in this auction.
     *
     */
    public void showLots()
    {
        if (lots.size() == 0){
            System.out.println("There are no lots.");
        }
        else
        {
            for(Lot lot : lots) {
                System.out.println(lot.toString());
            }
        }
    }
    
    /**
     * Bid for a lot.
     * Do not assume that the lots are stored in numerical order.
     * Prints a message indicating whether the bid is successful or not.
     *   
     * First print a blank line.  
     * Then print whether or not the bid is successful.
     * If the bid is successful, also print the
     * lot number, high bidder's name, and the bid value.
     * If the bid is not successful, also print the lot number 
     * and high bid (but not the high bidder's name).
     * 
     * Returns false if the auction is closed, the lot doesn't
     * exist, the bidder is null, or the bid was not positive
     * and true otherwise (even if the bid was not high enough).
     * (You need to update the return type, documentation, and code.)
     *
     * @param number The lot number being bid for.
     * @param bidder The person bidding for the lot.
     * @param value  The value of the bid.
     * 
     * @return true if the bid was submitted, false if the bid was not valid.
     */
    public boolean bidFor(int lotNumber, Person bidder, long value)
    {        
        Lot selectedLot = getLot(lotNumber);
        if(selectedLot != null && isOpen == true && bidder != null && value > 0 ) {
            Bid bid = new Bid(bidder, value);
            boolean successful = selectedLot.bidFor(bid);
            System.out.println("");
            if(successful) {
                System.out.println("The bid for lot number " +
                                   lotNumber + " by: " +
                                   bidder.getName() + " was successful.");
            }
            else {
                // Report which bid is higher.
                Bid highestBid = selectedLot.getHighestBid();
                System.out.println("Lot number: " + lotNumber +
                                   " already has a bid of: " +
                                   highestBid.getValue());
            }
            return true;
        }
        return false;
    }


    /**
     * Return the lot with the given number. 
     * Do not assume that the lots are stored in numerical order.   
     *   
     * Returns null if the lot does not exist.
     * (You need to update the code.)
     *
     * @param lotNumber The number of the lot to return.
     *
     * @return the Lot with the given number
     */
    public Lot getLot(int lotNumber)
    {
        for (Lot nextLot : lots){
            if (nextLot.getNumber() == lotNumber){
                return nextLot;
            }
        }
        return null;
    }
    
    /**
     * Closes the auction and prints information on the lots.
     * First print a blank line.  Then for each lot,
     * its number and description are printed.
     * If it did sell, the high bidder and bid value are also printed.  
     * If it didn't sell, that information is printed.
     *
     * Returns false if the auction is already closed, true otherwise.
     * (You need to update the return type, documentation, and code.)
     */
    public boolean close()
    {
        if (isOpen == false){
            return false;
        }
        
        for(Lot lot : lots){
            System.out.println("");

            Bid highestBid = lot.getHighestBid();
            
            if (highestBid == null){
                System.out.println("Lot number: " + lot.getNumber() + " did not sell.");
            }
            else
            {
                System.out.println("Lot number: " + lot.getNumber() +
                                           " has sold for: " +
                                           highestBid.getValue() +
                                           "to: " + highestBid.getBidder().getName());
            }
            
        }
        isOpen = false;
        return true;
    }
    
    /**
     * Returns an ArrayList containing all the items that have no bids so far.
     * (or have not sold if the auction has ended).
     * @return an ArrayList of the Lots which currently have no bids
     */
    public ArrayList<Lot> getNoBids()
    {
       ArrayList<Lot> tempArray = new ArrayList<Lot>();
       for (Lot nextLot : lots){
           if (nextLot.getHighestBid() == null){
               tempArray.add(nextLot);
            }
        }
       
       return tempArray; 
    }
    
    /**
     * Remove the lot with the given lot number, as long as the lot has
     * no bids, and the auction is open.  
     * You must use an Iterator object to search the list and,
     * if applicable, remove the item.
     * Do not assume that the lots are stored in numerical order.
     *
     * Returns true if successful, false otherwise (auction closed,
     * lot does not exist, or lot has a bid).
     *
     * @param number The number of the lot to be removed.
     * 
     * @return true if successful, false otherwise
     */
    public boolean removeLot(int number)
    {
        if (isOpen == false){
            return false;
        }
        
        for (Lot nextLot : lots){
            if (nextLot.getNumber() == number){
                if (nextLot.getHighestBid() != null){
                    return false;
                }
                return lots.remove(nextLot);
            }
        }
        return false;
    }
    
    /**
     * Returns if the asuction is currently open.
     */
    public boolean isOpen()
    {
        return isOpen;
    }
          
    /**
     * Returns the next lot number
     */
    public int nextLot()
    {
        return nextLotNumber;
    }
    
    
    
    
    
    
}






