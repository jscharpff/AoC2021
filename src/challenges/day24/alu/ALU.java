package challenges.day24.alu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import challenges.day24.alu.instr.ALUYieldException;
import challenges.day24.alu.instr.Instr;
import challenges.day24.alu.mem.ALUMem;
import challenges.day24.alu.mem.MemRegister;

/**
 * The Arithmetic Logical Unit, or ALU, of the submarine that is able to
 * process a set of simple instructions
 * 
 * @author Joris
 */
public class ALU {
	/** The current program that is loaded in memory of the ALU */
	private List<Instr> program;
	
	/** The memory registers of the running ALU */
	protected ALUMem mem;
	
	/** The current instruction pointer */
	private int ip;
	
	/** The current program yield state */
	private boolean yielded;
	
	/**
	 * Creates a new ALU
	 */
	public ALU( ) {
		program = null;
		ip = -1;
		mem = null;
		yielded = false;
	}
	
	/** @return The memory registers of the ALU */
	public ALUMem mem( ) { 
		if( mem == null ) throw new RuntimeException( "Memory is not initialised" );
		return mem;
	}
	
	/**
	 * Runs the loaded program with the given input. The input will be fed to the
	 * program from left to right. The memory registers persist after run for
	 * potential result output
	 * 
	 * @param input The program input
	 * @return The contents of all memory registers upon termination or yield
	 */
	public ALUMem run( final String input ) {
		if( program == null ) throw new RuntimeException( "No program loaded into ALU memory" );
		if( ip != -1 ) throw new RuntimeException( "Program is already running" );
		if( yielded ) throw new RuntimeException( "The program has yielded" );
		
		// reset memory and let the memory manager manage the program input
		mem = new ALUMem( MemRegister.values( ) );
		mem.setInput( input );
		
		// fake yield state and "continue" execution
		yielded = true;
		return cont( );
	}
	
	/**
	 * Continues the execution after a yield instruction
	 * 
	 * @return The contents of all memory registers upon termination or yield
	 */
	public ALUMem cont( ) {
		if( !yielded ) throw new RuntimeException( "The program was not yielded" );
		yielded = false;

		// run the program
		while( ++ip < program.size( ) ) {
			// get the next instruction to parse
			final Instr in = program.get( ip );
			
			try {
				
				// execute the instruction within the current context, i.e. the values
				// currently in the memory registers of the ALU
				in.execute( this.mem );
				
			} catch( ALUYieldException e ) {
				// yield instruction encountered, hence yield the execution
				yielded = true;
				return mem;
			}
		}
		
		// the program is done, reset IP to initial value to enable rerun
		return stop( );
	}
	
	/**
	 * Stops the current program execution
	 *
	 * @return The memory state upon termination
	 */
	public ALUMem stop( ) {
		yielded = false;
		ip = -1;
		final ALUMem memdump = mem;
		mem = null;
		return memdump;
	}

	/**
	 * Loads a program from a collection of Strings
	 * 
	 * @param input The program to load
	 */
	public void loadProgram( final Collection<String> input ) {
		final List<Instr> prog = new ArrayList<>( input.size( ) );
		
		// parse the instructions
		for( final String s : input )
			prog.add( Instr.parse( s ) );
		
		this.program = prog;
	}
}
