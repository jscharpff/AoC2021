package challenges.day24.alu.instr;

import challenges.day24.alu.mem.ALUMem;

/**
 * Simple instruction that throws an exception to halt the execution flow
 * immediately, after which the ALU unit transfers control to the process that
 * invoked the unit. After a yield, the program execution can be resumed by 
 * running <code>Alu.cont( )</code>.
 * 
 * @author Joris
 */
public class YieldInstruction extends Instr {

	/**
	 * Creates a new yield instruction
	 * 
	 * @param args The arguments (should be empty)
	 */
	public YieldInstruction( final IArg... args ) {
		super( args, 0 );
	}
	
	/**
	 * Overrides default execution behaviour, it only has to throw an exception
	 * instead of performing some computation
	 * 
	 * @throws ALUYieldException to yield execution
	 */
	@Override
	public void execute( final ALUMem alumem ) throws ALUYieldException {
		throw new ALUYieldException( );
	}
	
	/**
	 * Needs to be overwritten but is never called because the function 
	 * <code>Instr.execute( ALUMem alumem )</code> that calls it is overwritten
	 * by this type of instruction
	 * 
	 * @throws RuntimeException if the executeInstruction function is executed
	 *   because it should never be	
	 */
	@Override
	protected long executeInstruction( ) {
		throw new RuntimeException( "This function should not be called directly!" );
	}

}
