package bids;


public abstract class BidSystem {
	
	boolean isPassiveEnd(Sequence s) { return isEnd(s); }
	abstract boolean isEnd(Sequence s);
	abstract int nextPlayer(Sequence s);
	abstract Bid[] possibleMoves(Sequence s);	

}
