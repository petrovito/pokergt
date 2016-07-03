package algebra;

public interface SeparationOracle {
	
	
	public void set_center(VectorD v);
	public boolean is_in();
	public VectorD violation();
	

}
