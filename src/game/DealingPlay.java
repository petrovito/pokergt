package game;

import bids.Sequence;
import dealers.Dealing;

public class DealingPlay {
	
	public Dealing dealing_;
	public Sequence sequence_;
	
	
	public DealingPlay(Dealing dealing,Sequence sequence) {
		dealing_ = dealing;
		sequence_ = sequence;
	}
	
	public DealingPlay() { }
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DealingPlay) {
			DealingPlay dealing_play = (DealingPlay) obj;
			return dealing_play.dealing_.equals(dealing_)
					&& dealing_play.sequence_.equals(sequence_);
		}
		return super.equals(obj);
	}

}
