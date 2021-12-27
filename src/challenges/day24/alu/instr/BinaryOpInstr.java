package challenges.day24.alu.instr;

/**
 * Instruction that represents one of multiple binary operations
 * 
 * @author Joris
 */
public class BinaryOpInstr extends Instr {
	/** The binary operand that is to be performed by this instruction */
	private final BinaryOperators op;
	
	/**
	 * Creates a new binary operation
	 * 
	 * @param op The operation to perform
	 * @param args The arguments to perform operation on
	 */
	public BinaryOpInstr( final BinaryOperators op, final IArg... args ) {
		super( args, 2 );
		
		this.op = op;
	}
	
	/**
	 * Executes the operation
	 * 
	 * @return The resulting value of the operation 
	 */
	@Override
	protected long executeInstruction( ) {
		// get actual values of the arguments
		final long[] v = new long[] { arguments[0].getValue( mem ), arguments[1].getValue( mem ) };
		
		switch( op ) {
			case Add: return v[0] + v[1];
			case Multiply: return v[0] * v[1];
			case Divide: return (long)(v[0] / v[1]);
			case Modulo: return v[0] % v[1];
			case Equals: return v[0] == v[1] ? 1 : 0;
			
			default:
				throw new IllegalArgumentException( "Unsupported operation: " + op );
		}
	}
}
