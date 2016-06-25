package game;

import java.util.ArrayList;
import java.util.Arrays;

import bids.Bid;

public class GamePlay extends ArrayList<DealingPlay> {

	private static final long serialVersionUID = -5216638401515725089L;
	
	private Game game_;
	
	public GamePlay(Game game) { game_ = game;}
	
	public GamePlay(Game game, DealingPlay... dealing_plays) {
		super(Arrays.asList(dealing_plays));
		game_ = game;
	}
	
	public GamePlay(GamePlay game_play) {
		super(game_play);
		game_ = game_play.game_;
	}
	
	
	public boolean equals(Object o) {
		if (o instanceof GamePlay) {
			GamePlay game_play = (GamePlay) o;
			if (size() != game_play.size())
				return false;
			for (int i = 0; i < size(); i++) {
				if (!get(i).equals(game_play.get(i)))
					return false;
			}
			return true;
		}
		return super.equals(o);
	}
	
	public DealingPlay last() {
		return get(size()-1);
	}
	
	public boolean is_game_end() {
		if (last().sequence_.is_game_end()) return true;
		return last().sequence_.is_bid_end() && last().dealing_.is_end();
	}
	
	public boolean is_bidding_end() {
		return last().sequence_.is_bid_end();
	}

	public int next_player() {
		if (!is_bidding_end()) return last().sequence_.next_player();
		return game_.biddings_.get(size()).first_player();
	}

	public GamePlay copy(DealingPlay dealing_play) {
		GamePlay game_play = new GamePlay(this);
		game_play.add(dealing_play);
		return game_play;
	}

	public GamePlay copy(Bid bid) {
		GamePlay game_play = new GamePlay(this);
		game_play.last().sequence_.add(bid);
		return game_play;
	}
	

}
