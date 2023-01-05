package challenges.day24.alu.mem;

/**
 * Available memory registers
 */
public enum MemRegister {
	W, X, Y, Z;

	/**
	 * @param index The register index
	 * @return The parsed memory register
	 */
	public static MemRegister parse( final String index ) {
		final MemRegister r = MemRegister.valueOf( index.toUpperCase( ) );
		if( r == null ) throw new IllegalArgumentException( "Invalid memory register: " + index );
		return r;
	}
}
