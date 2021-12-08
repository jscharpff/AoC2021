package util;

import java.util.stream.Stream;

/**
 * Common helper functions
 * 
 * @author Joris
 */
public class Util {
	/**
	 * Parses a comma separated string into an int array
	 * 
	 * @param str The string value
	 * @return An array of ints
	 */
	public static int[] toIntArray( final String str ) {
		return Stream.of( str.split( "," ) ).mapToInt( x -> Integer.valueOf( x ) ).toArray( );
	}
		
	/**
	 * Reverses a string
	 * 
	 * @param str The string to revers
	 * @return The reversed string
	 */
	public static String reverseString( final String str ) {
		 return new StringBuffer( str ).reverse( ).toString( );
	}
	
	/**
	 * Returns the string difference of the two strings, i.e. the characters that
	 * are not in both strings
	 * 
	 * @param s1
	 * @param s2
	 * @return The string difference
	 */
	public static String stringDifference( final String s1, final String s2, final boolean bothways ) {
		String result = "";
		for( int i =  0 ; i < s1.length( ); i++ )
			if( !s2.contains( "" + s1.charAt( i ) ) ) result += s1.charAt( i );
		if( bothways )
			for( int i =  0 ; i < s2.length( ); i++ )
				if( !s1.contains( "" + s2.charAt( i ) ) ) result += s2.charAt( i );
		return result;
	}
	
	/**
	 * Returns the string difference of s1 - s2, i.e. all characters in s1 that are not in s2
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String stringDifference( final String s1, final String s2 ) {
		return stringDifference( s1, s2, false );
	}
	
	/**
	 * Checks if the input string contains all characters in the check string,
	 * regardless of order
	 * 
	 * @param input The input string to check
	 * @param check The string to check against the input
	 * @return True iff all characters of the check string are in the input
	 */
	public static boolean stringContainsAll( final String input, final String check ) {
		for( int i = 0; i < check.length( ); i++ )
			if( !input.contains( "" + check.charAt( i ) ) ) return false;
		
		return true;
	}
}
