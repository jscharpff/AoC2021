package challenges.day24.alu.instr;

import challenges.day24.alu.mem.ALUMem;

/**
 * Class to hold an instruction for the ALU unit
 * 
 * @author Joris
 */
public abstract class Instr {
	/** The arguments to the instruction */
	protected IArg[] arguments;
	
	/** Reference to the ALU's memory, to be used by instructions */
	protected ALUMem mem;
	
	/**
	 * Creates a new instruction with the given arguments
	 * 
	 * @param args The instruction arguments
	 * @param numargs The number of expected arguments, to check
	 */
	protected Instr( final IArg[] args, final int numargs ) {
		if( args.length != numargs ) throw new RuntimeException( "Invalid number of arguments, expected 1 but received " + args.length );

		this.arguments = args;
	}
	
	
	/**
	 * Executes the instruction and writes the result into the memory register
	 * specified by the first argument
	 * 
	 * @param alumem The current memory context of the ALU unit 
	 * @throws ALUYieldException if the execution must yield
	 */
	public void execute( final ALUMem alumem ) throws ALUYieldException {
		this.mem = alumem;
		alumem.write( arguments[0].getRegister( ), executeInstruction( ) );
		this.mem = null;
	}
	
	/**
	 * Executes the instruction
	 * 
	 * @return The value as a result of the instruction, which will be written to
	 *   the memory register of the first argument
	 */
	protected abstract long executeInstruction( );
	
	/**'
	 * The core of the instruction parsing, translates the textual instruction
	 * into an ALU Instruction object that can be executed.
	 * 
	 * @param input The instruction to parse
	 * @return The parsed instruction
	 */
	public static Instr parse( final String input ) {
		final String[] in = input.toLowerCase( ).split( " " );
		
		// parse arguments
		final IArg[] args = new IArg[ in.length - 1 ];
		for( int i = 0; i < args.length; i++ )
			args[i] = IArg.parse( in[ i + 1 ] );

		// check if first argument is an memory register, if it has arguments
		if( args.length != 0 && args[0].isLiteral( ) )
			throw new IllegalArgumentException( "First argument needs to be a memory register" );

		// determine the instruction to parse
		final String i = in[0];

		// simple input instruction that reads the next program input
		if( i.equals( "inp" ) ) return new InputInstr( args );
		
		// yield instruction
		if( i.equals( "yield" ) ) return new YieldInstruction( args );
			
		// check for binary operands
		try {
			final BinaryOperators op = BinaryOperators.fromInstruction( i );
			return new BinaryOpInstr( op, args );
		} catch( Exception e ) { /* ignore, not a binary operand */ }
		
		// not supported!
			throw new RuntimeException( "Unsupported instruction in program: " + input );
	}
	
}
