package game;

import bids.Sequence;
import dealers.Dealing;

public class DealingPlay {
	
	public Dealing dealing_;
	public Sequence sequence_;
	
	
	public DealingPlay(Dealing dealing, Sequence sequence) {
		dealing_ = dealing;
		sequence_ = sequence;
	}

	public DealingPlay() { }
	
	
	public DealingPlay copy() {
		DealingPlay dealing_play = new DealingPlay();
		dealing_play.sequence_ = new Sequence(sequence_);
		dealing_play.dealing_ = new Dealing(dealing_);
		return dealing_play;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DealingPlay) {
			DealingPlay dealing_play = (DealingPlay) obj;
			return dealing_play.dealing_.equals(dealing_)
					&& dealing_play.sequence_.equals(sequence_);
		}
		return super.equals(obj);
	}
	
	
	@Override
	public String toString() {
		return "("+dealing_+"::"+sequence_+")";
	}

}
