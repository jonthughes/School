import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

/**
 * SC4 CPU
 * TCSS 372 A
 * Autumn 2015
 * By: Jonathan Hughes, David Humphreys, Artem Davtyan
 * 
 * The SC4_CPU class simulates the control unit of the SC4 finite state machine.
 */
public class SC4_CPU extends Observable implements Observer {
	
    /** Constant int values for the Control Unit's macrostates */
	private final int FETCH = 0;	//Represents the Fetch macrostate
	private final int DECODE = 1;	//Represents the Decode macrostate
	private final int EXECUTE = 2;  //Represents the Execute macrostate
	private final int HALT = 3;		//Represents the Halt macrostate
	
	/** Constant int values for Fetch's microstates */
	private final int ifetch1 = 0;	//Represents Fetch's first microstate
	private final int ifetch2 = 1;	//Represents Fetch's second microstate
	private final int ifetch3 = 2;  //Represents Fetch's third microstate
	private final int ifetch4 = 3;  //Represents Fetch's fourth microstate
	private final int MICROSTATE_EXIT = 4;	//Represents Fetch's microstates are over
	
	/** Constant int values for OPCODES */
	private final int LDI_OPCODE = 1;	//OPCODE for the LDI instruction
	private final int LD_OPCODE = 2;	//OPCODE for the LD instruction
	private final int ST_OPCODE = 4;	//OPCODE for the ST instruction
	private final int MOV_OPCODE = 5;   //OPCODE for the MOV instruction
	private final int ADD_OPCODE = 8;	//OPCODE for the ADD instruction
	private final int SUB_OPCODE = 9;	//OPCODE for the SUB instruction
	private final int AND_OPCODE = 10;	//OPCODE for the AND instruction
	private final int OR_OPCODE = 11;	//OPCODE for the OR instruction
	private final int NOT_OPCODE = 12;	//OPCODE for the NOT instruction
	private final int BR_OPCODE = 16;   //OPCODE for the BR instruction
	private final int BRZ_OPCODE = 17;  //OPCODE for the BRZ instruction
	private final int HALT_OPCODE = 29; //OPCODE for the HALT instruction
	
	/** Constant int values for instruction format */
	private final int FORMAT_1 = 1;	//Represents Format 1 Type instruction
	private final int FORMAT_2 = 2;	//Represents Format 2 Type instruction
	private final int FORMAT_3 = 3;	//Represents Format 3 Type instruction
	private final int FORMAT_4 = 4;	//Represents Format 4 Type instruction
	
	/** Constant values for two's complement min and max (for checking overflow) */
	private final long TWOS_COMP_MIN = -2147483648; 
	private final long TWOS_COMP_MAX = 2147483647;
    
	/** Memory and Register Files */
	private final int MEMORY_SIZE = 1000; //The size of the program's memory
	private final int REGFILE_SIZE = 16;  //The size of the program's register file
	private long[] MEMORY;                //Contains the program's memory as an array of longs
	private long[] RegFile;               //Contains the register file
    private String TEXT_FILE;             //The text file that contains the program
    private boolean RUN;                  //Whether program is set to run continuously
	private long MEMORY_DUMP_START;       //The starting index of the memory dump
	
	/** I/O Memory Mapped Files */
	//Base I/O
	private final long IOBASE = Long.parseLong("2952790016"); //The I/O registers' base in memory =xB0000000
	private final int IOMEMORY_SIZE = 16;         //The amount of memory for I/O devices
	private long[] IOMEMORY;                      //A simulated space in memory for I/O registers
	//TIMER
	private final int TIMER0 = 0;                 //The timer's offset from IOBASE
	private final int TIME_COUNTER_DEFAULT = 100; //The default time counter value
	private boolean timerInterruptMode;           //Whether time interrupt mode is on (vs polling)
	private boolean timerResetMode;               //Whether time reset mode is on
	private boolean timerCountComplete;           //Whether time count complete mode is on
	private long timerCount;                      //The current count of the timer
	//KBD
	private final int KBD = 4;                    //The keyboard's offset from IOBASE
    private boolean kbdReady;                     //Whether KBD ready bit is on or off
	private char kbdData;                         //ASCII char stored in kbd data register
	private boolean kbdInterruptMode;             //Whether KBD interrupt mode is on (vs polling)
	private final int KBD_STALL = 1;              //Amount of cycles to stall keyboard
	private int kbdStall;                         //For stalling the keyboard input
	//SCRN
	private final int SCRN = 8;                   //The screen's offset from IOBASE
	private boolean scrnReady;                    //Whether SCRN ready bit is on or off
	private char scrnData;                        //ASCII char stored in SCRN data register
	private boolean scrnInterruptMode;            //Whether SCRN interrupt mode is on (vs polling)
	//COM1
	private final int COM1 = 12;                  //The COM1's offset from IOBASE	
    
	/** Control Unit States */
	private int state;				//Represents the Control Unit's current macrostate
	private int microstate;			//Represents the Control Unit's current microstate
	
	/** Datapath Elements */
	private long PC;			    //Contains the Program Counter's contents
	private long IR;			    //Contains the Instruction Register's contents
	private String IRstring;		//Contains the Instruction Register's contents as a 
									//binary String for decoding purposes
	private long MAR;			    //Contains the Memory Address Register's contents
	private long MDR;               //Contains the Memory Data Register's contents
	private long ALU_A; 		    //Contains the ALU's A Register's contents
	private long ALU_B;		        //Contains the ALU's B Register's contents
	private long ALU_R;             //Contains the ALU's Result Register's contents
	
	/** Condition Code Bits */
	private long SW;                //Contains the Status Word Register's contents
    private boolean CC_Zero;        //True if result is zero
    private boolean CC_Neg;         //True if result is negative
    private boolean CC_Carryout;    //True if result has a carryout
    private boolean CC_Overflow;    //True if result has an overflow
    
	/** Decoded Elements from Instruction */
	private int opcode;				//Contains the opcode of the instruction
	private int format;				//Contains the format of the instruction
	private int dr;				    //Contains the destination register of the instruction
	private int sr1;				//Contains the source register 1 of the instruction
	private int sr2;				//Contains the source register 2 of the instruction
	private int immed;				//Contains the immediate value of the instruction	
	
	/** Scanner for console input */
	private Scanner inputReader = new Scanner(System.in);
		
	/**
     * This is the main method to start the program.  It creates a new SC4_CPU object 
     * to start the CPU's cycle.
     * 
     * @param args Arguments from command line.
     */
    public static void main(String[] args) {
        new SC4_CPU(); //start CPU
    }
    
	/**
	 * Constructs an FSM_Control_Unit object which simulates the LC-2200 control unit.  
	 * It instantiates all the registers and memory.  It also sets Control unit's current state 
	 * to FETCH, microstate to 0, and instruction elements to 0, etc.  It then runs runCycle(). 
	 */
	public SC4_CPU() {
	    //initialize CPU's registers
		PC = 0;
		IR = 0;
		MAR = 0;
		MDR = 0;
		ALU_A = 0;
		ALU_B = 0;
		ALU_R = 0;
		SW = 0;
		
		//initialize CPU specific variables
		opcode = 0;
		format = 0;
		dr = 0;
		sr1 = 0;
		sr2 = 0;
		immed = 0;
		RUN = false;
		
		//initialize memory
		MEMORY_DUMP_START = 0;
		RegFile = getRandLongArray(REGFILE_SIZE);
		MEMORY = getRandLongArray(MEMORY_SIZE);
		IOMEMORY = new long[IOMEMORY_SIZE];
		setTimeCounterResetValue(TIME_COUNTER_DEFAULT);
		
		//initialize IO devices
		timerCount = getTimeCounter();
		timerInterruptMode = false;  
	    timerResetMode = true;    
	    timerCountComplete= false; 
	    kbdReady = true;
	    kbdData = 0;
	    kbdInterruptMode = false;
	    kbdStall = KBD_STALL;
	    scrnReady = true;  
	    scrnData = 0;          
	    scrnInterruptMode = false;
	    SC4_SCRN_KBD screen = new SC4_SCRN_KBD();
        this.addObserver(screen);
        screen.addObserver(this);
        
        //initialize debug screen
		printState();
        pause();
		state = FETCH;
        microstate = 0;
        
        //start CPU run cycle
        runCycle();
	}

    /**
     * Creates a new long array of specified size, filled with random long's up to 2^31-1.
     * 
     * @param size - size of array to to fill with random longs
     * @return long[] of random long's
     */
    private long[] getRandLongArray(int size) {
	    long[] result = new long[size]; //Initialize array of input size
	    for (int i = 0; i < result.length; i++) { //Go through each index
            result[i] = (long) (TWOS_COMP_MAX * Math.random()); //Initialize each index to random  
	    }
        return result;
    }
    
    /**
     * Loads the Memory as an array of "long"s.  The long's are pulled from individual lines of
     * the specified txt file from the same folder as the program. 
     * 
     * @param inputFile - file name of text file that contains the memory.
     * @return long[] of lines from memory converted from hex to long
     */
    private long[] getMemory(String inputFile) {
	    long[] result = MEMORY;
	    int iterator = 0;
	    //Bring in text file to be read
	    InputStream input = getClass().getResourceAsStream(inputFile); 
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line;
        try { //Read through each line of text file
            while ((line = reader.readLine()) != null) { //While there is another line to read
                result[iterator] = getInstruction(line); //Put line's instruction into array
                iterator++; //Iterate array, so next line is put at next index
            }
        } catch (IOException e) { //Catch exceptions caused by reading the file
            System.out.println("Could not read file!");
            e.printStackTrace();
        }
        try {
            reader.close(); //Close file when done reading it
        } catch (IOException e2) { //Catch exceptions caused by closing the file
            e2.printStackTrace();
        }
        return result;
    }
	
    /**
     * Returns the instruction from a give String or "line" of the input text.  It returns the
     * long value of the hex portion of the line.
     * 
     * @param line - the line of text that contains an instruction
     * @return long that represents the long value of the hex that the line contains
     */
    private long getInstruction(String line) {
        int colonIndex = line.indexOf(':'); //finds the first colon (end of address)
        line = line.substring(colonIndex + 1); //cuts off address and colon to get just the instruction
        //Checks input line to see if they entered non-hex digits
        if (!line.matches("^[0-9A-Fa-f]+$")) { 
            System.out.println("\"" + line + "\" has invalid hex digits!"); 
        //If user inputs more than 8 digits, warn that it is too long.
        } else if (line.length() > 8) { 
            System.out.println(line + " is too long!"); 
        //If user inputs an invalid opcode, warn them
        } else { //Valid user input
            return Long.parseLong(line, 16); //Parse Hex String input into a BigInteger       
        }
        return 0; //If invalid command, return "NO OP"
    }
    
    /**
	 * Simulates the Control Unit's Fetch-Decode-Execute cycle.  It runs the fetch macrostate, 
	 * then decode macrostate, then execute macrostate, then back to fetch, etc until the 
	 * state is changed to HALT.
	 */
	private void runCycle() {
	    while (state != HALT) {//Run the FSM's cycle until state is changed to HALT
		    switch (state) {//Switch to macrostate that is the current state of the Control Unit
		        case FETCH: //Runs the fetch() method which simulates Fetch's microstates
		                fetch();
		                break;
		                
		        case DECODE: //Runs the decode() method which simulates Decode's microstates
		                decode();
		                break;
		        
		        case EXECUTE: //Runs the execute() method which simulates Execute's microstates
		                execute();
		                break;
		                 
		        default: 
		                break;
			}
        }
	}

	/**
	 * Simulates the Fetch macrostate by running its microstates.  Also decrements timer.
	 */
	private void fetch() {
	    checkIO(); //run I/O processes once per cycle
                
		microstate = ifetch1;
		if (PC >= MEMORY.length) {//in case we run into end of code, halts machine
            state = HALT;
            microstate = MICROSTATE_EXIT;
        }
		while (microstate != MICROSTATE_EXIT) {
			switch (microstate) {
				case ifetch1: //Runs the ifetch1 microstate
    				    MAR = PC;
    			        ALU_A = PC;
    			        microstate = ifetch2;
    			        
    			        break;
	                
				case ifetch2: //Runs the ifetch2 microstate
	                	IR = MEMORY[(int) PC]; //Load the IR with contents of memory at PC's address
	                	microstate = ifetch3;
		                break;
		        
				case ifetch3: //Runs the ifetch3 microstate
    				    PC = ALU_A + 1; //Adds one to ALU_A and stores it in the PC
    			        microstate = ifetch4;
						break;
	                
				case ifetch4: //Runs the ifetch4 microstate
    				    state = DECODE;
    			        microstate = MICROSTATE_EXIT;
		                break;
		                 
		        default: 
		            	break;
			}
		}		     
	}
	


    /**
	 * Simulates the Decode macrostate by running its microstates.  The state is changed 
	 * to EXECUTE when complete.
	 */
	private void decode() {
		//Run the getIRstring method to get the string version of the instruction
		IRstring = longToBinaryString(IR);
		opcode = Integer.parseInt(IRstring.substring(0, 5), 2); //Convert opcode from binary to int
		//Run the getFormat method to get the format of the instruction
		format = getFormat(IRstring);	
		switch (format) {
			case FORMAT_1: //When a Format 1 instruction is given, set the destination register and immediate value
			        dr = Integer.parseInt(IRstring.substring(5, 9), 2); //Convert dr from binary to int
			        immed = (int)twosCompToLong(IRstring.substring(9, 32)); //Convert immed from binary to int
			        break;
		        
			case FORMAT_2: //When a Format 2 instruction is given, set the destination register, source register 1, 
			               //and immediate value
			        dr = Integer.parseInt(IRstring.substring(5, 9), 2); //Convert dr from binary to int
			        sr1 = Integer.parseInt(IRstring.substring(9, 13), 2); //Convert sr1 from binary to int
			        immed = (int)twosCompToLong(IRstring.substring(13, 32)); //Convert immed from binary to int  
		            break;
		    
			case FORMAT_3: //When a Format 3 instruction is given, set the destination register, source register 1, 
			               //and source register 2
			        dr = Integer.parseInt(IRstring.substring(5, 9), 2); //Convert dr from binary to int
			        sr1 = Integer.parseInt(IRstring.substring(9, 13), 2); //Convert sr1 from binary to int
			        sr2 = Integer.parseInt(IRstring.substring(13, 17), 2); //Convert sr2 from binary to int
					break;
		        
			case FORMAT_4: //When an O-TYPE instruction is given, no registers need to be set
			        immed = (int)twosCompToLong(IRstring.substring(5, 32)); //Convert immed from binary to int  
		            break;

		    default: 
		        	break;
		}
		state = EXECUTE;
	}
	
    /**
     * Converts the long to a binary string.  It extends the 0's of the left if there are 
     * not enough (should be of length 32).  Not for two's complement.
     * Returns the resulting string.
     * 
     * @param input - long to convert to binary string (not two's complement)
     * @return String binary string representation of input long (not two's complement)
     */
    private String longToBinaryString(long input) {
        String result = Long.toString(input, 2); //Converts Instruction Register to binary
        while (result.length() < 32) { //Extends 0's, if there are not enough 0 bits
            result = "0" + result;
        }
        return result;
    }

    /**
	 * Converts a binary two's complement number to a Java long.
	 * The first number of a two's complement shows if the long is positive or negative.
	 * If negative, you subtract 2^(#of bits minus one).
	 * 
	 * @param twosCompString - a String binary two's complement number
	 * @return long that represents the input binary 2's complement
	 */
	private long twosCompToLong(String twosCompString) {
	    int firstNumber = Integer.parseInt(twosCompString.substring(0, 1), 2);
	    long result = firstNumber;
	    if (twosCompString.length() > 1) {
    	    int length = twosCompString.length();
    	    long restOfNumber = Long.parseLong(twosCompString.substring(1, length), 2);
    	    result = restOfNumber; //if first number is zero, this is the resulting int
    	    if (firstNumber == 1) { //if first number is one, it's negative, so subtract 2^(#of bits minus one)
    	        result = (long) (restOfNumber - Math.pow(2, length-1));
    	    }
	    }
        return result;
    }

    /**
	 * Converts the input long into a 2's complement binary string.  It sign extends the 0's or 1's depending on 
	 * if the number is negative or positive.
	 * Returns the resulting binary string.
	 * 
	 * @param input - long to convert to two's complement number
	 * @return String binary two's complement number that represents the input long
	 */
	private String longToTwosComp(long input) {
	    String result = Long.toBinaryString(input); //convert input to binary string
	    while (result.length() < 64) { //add 0's to get 64 bits, if less than 64
	        result = "0" + result;
	    }
	    result = result.substring(32, 64); //keep only last 32 bits
	    return result;
	}
	
	/**
	 * Computes the opcode from the IR.
	 * 
	 * @param IRString - String representing the Instruction Register's contents
	 * @return the format of the instruction based on the opcode as an int (eg FORMAT_1, FORMAT_2, etc).
	 */
	private int getFormat(String IRstring) {
		
		if (opcode == LDI_OPCODE) {
			return FORMAT_1;
		} else if (opcode == LD_OPCODE || opcode == ST_OPCODE || opcode == NOT_OPCODE) {
			return FORMAT_2;
		} else if (opcode == ADD_OPCODE || opcode == SUB_OPCODE || opcode == AND_OPCODE 
		        || opcode == OR_OPCODE || opcode == MOV_OPCODE) {
			return FORMAT_3;
		} else {
			return FORMAT_4;
		}
	}
	
	/**
	 * Simulates the Execute macrostate by outputting the specified instruction and what it would 
	 * be doing to the console. If the HALT instruction is called, the state is set to HALT, 
	 * otherwise it is set to FETCH.
	 */
	private void execute() {
	    String lastOP = " RTL: ";
	    switch (opcode) {
		    case LDI_OPCODE: //Load indirect
		        lastOP += "(LDI) R" + dr + " <- " + immed;
				RegFile[dr] = immed;
		        break;
		        
			case LD_OPCODE: //Load base-relative
			    ALU_A = RegFile[sr1];
			    ALU_B = immed;
			    ALU_R = ALU_A + ALU_B;
//			    ALU_R = twosCompToLong(longToTwosComp(ALU_R)); //ensures only 32 bits
//			    System.out.println(longToHex(ALU_A)+" "+longToHex(ALU_B) +" " +longToHex(IOBASE));
//			    System.out.println(ALU_A+" "+ALU_B +" " +IOBASE);
//			    System.out.println(longToHex(ALU_R));
//			    System.out.println(ALU_R);
//			    System.out.println(longToHex(IOBASE + 15));
//			    System.out.println(IOBASE + 15);
//			    System.out.println((ALU_R >= IOBASE) + " " + (ALU_R <= (IOBASE+15)));
			    if (ALU_R >= IOBASE && ALU_R <= (IOBASE + 15)) {//if address in IO range
			        lastOP += "(LD(IO)) R" + dr + " <- MEM[R" + sr1 + " + "+ immed + "]";
                    RegFile[dr] = IOMEMORY[(int) (ALU_R - IOBASE)];
			    } else if (ALU_R > MEMORY.length || ALU_R < 0) {//not in IO range
				    lastOP += "(LD) Error, memory out of bounds at: " + ALU_R;
				} else {
				    lastOP += "(LD) R" + dr + " <- MEM[R" + sr1 + " + "+ immed + "]";
				    RegFile[dr] = MEMORY[(int) ALU_R];
				}
				break;
	        	
			case ST_OPCODE: //Store base-relative
			    ALU_A = RegFile[sr1];
                ALU_B = immed;
                ALU_R = ALU_A + ALU_B;
//                ALU_R = twosCompToLong(longToTwosComp(ALU_R)); //ensures only 32 bits
                if (ALU_R >= IOBASE && ALU_R <= (IOBASE + 15)) {//if address in IO range
                    lastOP += "(ST(IO)) R" + dr + " <- MEM[R" + sr1 + " + "+ immed + "]";
                    MEMORY[(int) (ALU_R - IOBASE)] = RegFile[dr];   
                } else if (ALU_R > MEMORY.length || ALU_R < 0) {//not in IO range
                    lastOP += "(ST) Error, memory out of bounds at: " + ALU_R;
			    } else {
                    lastOP += "(ST) MEM[R" + sr1 + " + "+ immed + "] <- R" + dr;
                    MEMORY[(int) ALU_R] = RegFile[dr];                    
                }
	        	break;
	        	
			case MOV_OPCODE: //Move register to register
                lastOP += "(MOV) R" + dr + " <- R" + sr1;
                ALU_A = RegFile[sr1];
                RegFile[dr] = ALU_A;
                break;
	        	
			case ADD_OPCODE: //Addition
			    lastOP += "(ADD) R" + dr + " <- R" + sr1 + " + R" + sr2;
				ALU_A = RegFile[sr1];
				ALU_B = RegFile[sr2];
				ALU_R = ALU_A + ALU_B;
				setCC(ALU_R);
				ALU_R = twosCompToLong(longToTwosComp(ALU_R)); //ensures only 32 bits
				RegFile[dr] = ALU_R;
				break;
	        	
			case SUB_OPCODE: //Subtraction
			    SW = 0;
			    lastOP += "(SUB) R" + dr + " <- R" + sr1 + " - R" + sr2;
			    ALU_A = RegFile[sr1];
                ALU_B = RegFile[sr2];
                ALU_R = ALU_A - ALU_B;
                setCC(ALU_R);
                ALU_R = twosCompToLong(longToTwosComp(ALU_R)); //ensures only 32 bits
                RegFile[dr] = ALU_R;
                break;
	        	
			case AND_OPCODE: //Bit-wise AND
			    lastOP += "(AND) R" + dr + " <- R" + sr1 + " AND R" + sr2;
			    ALU_A = RegFile[sr1];
                ALU_B = RegFile[sr2];
                String binA = longToTwosComp(ALU_A);
                String binB = longToTwosComp(ALU_B);
                String binR = "";
                for (int i = 0; i < binA.length(); i++) {
                    if (binA.charAt(i) == binB.charAt(i) && binA.charAt(i) != '0') {
                        binR = binR + "1";
                    } else {
                        binR = binR + "0";
                    }
                }
                ALU_R = twosCompToLong(binR);
                setCC(ALU_R);
                RegFile[dr] = ALU_R;
                break;
	        	
			case OR_OPCODE: //Bit-wise OR
			    lastOP += "(OR) R" + dr + " <- R" + sr1 + " OR R" + sr2;
			    ALU_A = RegFile[sr1];
                ALU_B = RegFile[sr2];
                binA = longToTwosComp(ALU_A);
                binB = longToTwosComp(ALU_B);
                binR = "";
                for (int i = 0; i < binA.length(); i++) {
                    if (binA.charAt(i) == 1 || binB.charAt(i) == 1) {
                        binR = binR + "1";
                    } else {
                        binR = binR + "0";
                    }
                }
                ALU_R = twosCompToLong(binR);
                setCC(ALU_R);
                RegFile[dr] = ALU_R;
                break;
	        	
            case NOT_OPCODE: //Bit-wise NOT
                lastOP += "(NOT) R" + dr + " <- NOT R" + sr1;
                ALU_A = RegFile[sr1];
                binA = longToTwosComp(ALU_A);
                binR = "";
                for (int i = 0; i < binA.length(); i++) {
                    if (binA.charAt(i) == 0) {
                        binR = binR + "1";
                    } else {
                        binR = binR + "0";
                    }
                }
                ALU_R = twosCompToLong(binR);
                setCC(ALU_R);
                RegFile[dr] = ALU_R;
                break;
                
            case BR_OPCODE: //Unconditional branch
                lastOP += "(BR) PC <- iterated PC + " + immed;
                ALU_A = PC;
                ALU_B = immed;
                ALU_R = ALU_A + ALU_B;
                ALU_R = twosCompToLong(longToTwosComp(ALU_R)); //ensures only 32 bits
                PC = ALU_R;
                break;
                
            case BRZ_OPCODE: //Branch on zero
                lastOP += "(BRZ) ";
                if (CC_Zero) {
                    ALU_A = PC;
                    ALU_B = immed;
                    ALU_R = ALU_A + ALU_B;
                    ALU_R = twosCompToLong(longToTwosComp(ALU_R)); //ensures only 32 bits
                    PC = ALU_R;
                    lastOP += "PC <- iterated PC + " + immed;
                }
                else {
                    lastOP += "Did not branch";
                }
                break;
	        	
			case HALT_OPCODE: //Halt
			    lastOP += "(HALT) Halting Program...";
				state = HALT;
	        	break;
           
		    default: 
		        lastOP += "(NOP) No operation";
		        	break;
		}
	    if (state != HALT) { //Only change state to FETCH if HALT is not chosen
	        printState();
            System.out.print(lastOP);
            System.out.print("     Binary: " + IRstring);
			state = FETCH; //Set state to FETCH after Execute is complete
			if (!RUN) {
			    pause();
			}
		}	
	}

	/**
     * Sets condition codes based on long result from an operation.
     * 
     * @param input - long result from an operation
     */
    private void setCC(long input) {
        CC_Zero = (input == 0);   
        CC_Neg = (input < 0);
        CC_Carryout = (input > TWOS_COMP_MAX || input < TWOS_COMP_MIN); //if too large or too small
        CC_Overflow = (input > TWOS_COMP_MAX || input < TWOS_COMP_MIN); //if too large or too small
        setSW();
    }
    
    /**
     * Sets the SW word based on current values of condition code (CC) flags for 
     * CC_Zero, CC_Neg, CC_Carryout, and CC_Overflow.  This is represented by the first
     * four bits of the SW word, respectively. (The rest is just 0's.)
     */
    private void setSW() {
        String SWstring = "";
        if (CC_Zero) { //If result is 0, turn on CC_Zero flag
            SWstring += "1";
        } else { //Else, turn off CC_Zero flag
            SWstring += "0";
        } 
        if (CC_Neg) { //If result is negative, turn on CC_Neg flag
            SWstring += "1";
        } else { //Else, turn off CC_Neg flag
            SWstring += "0";
        } 
        if (CC_Carryout) { //If result has a carryout, turn on CC_Carryout flag
            SWstring += "1";
        } else { //Else, turn off CC_Carryout flag
            SWstring += "0";
        } 
        if (CC_Overflow) { //If result has an overflow, turn on CC_Overflow flag
            SWstring += "1";
        } else { //Else, turn off CC_Overflow flag
            SWstring += "0";
        } 
        SWstring += "000000000000000000000000000"; //Fill in extra bits as 0
        SW = Long.parseLong(SWstring, 2); //Convert from binary to long for storage   
    }
    
    /**
	 * Prints the state of the machine into the console.
	 */
	private void printState() {
	    for (int j = 0; j < 24; j++) { //Print 24 line spacer
	        System.out.println();
	    }
	    System.out.println(" Hughes, Humphreys, Davtyan SC-4 Debug Monitor");
        System.out.println(" Timer: "+timerCount + ", KBD Ready: " + kbdReady + ", KBD Data: " + kbdData 
                + ", SCRN Ready: " + scrnReady + ", SCRN Data: " + scrnData);
	    System.out.println(" Register File                  Memory Dump");
	    for (int i = 0; i < RegFile.length; i++) { //iterate through RegFile registers to append them
            System.out.println(" " + Integer.toHexString(i).toUpperCase() + ": " + longToHex(RegFile[i]) + 
                    "                 " + longToHex((long)(MEMORY_DUMP_START + i)) + ": " + longToHex(MEMORY[(int) (MEMORY_DUMP_START + i)]));
        }
	    System.out.println();
	    System.out.println(" PC: " + longToHex(PC) + "  IR: " + longToHex(IR) + " SW: " + longToHex(SW));
	    System.out.println(" MAR: " + longToHex(MAR) + " MDR: " + longToHex(MDR) + " ALU.A: " + longToHex(ALU_A) 
	                        + " ALU.B: " + longToHex(ALU_B) + " ALU.R: " + longToHex(ALU_R));
        
       
    }

	/**
	 * The pause method pauses the CPU's run cycle and waits for the user to press enter to continue.
	 */
    private void pause() { 
        System.out.println();
        System.out.println(" Commands: 1=Load, 2=Step, 3=Run, 4=Memory, 5=Save, 6=Edit, 9=Terminate");
        System.out.println();
        System.out.print("Enter: ");
	    try {
	        String input = inputReader.nextLine().trim(); //Get user input
	        switch (input){
	            case "1": //Load file name
	                System.out.print("Enter name of file to load: ");
	                TEXT_FILE = inputReader.nextLine().trim();
	                //if file does not end in .txt, put .txt on the end
	                if (!TEXT_FILE.toLowerCase().endsWith(".txt")) { 
	                    TEXT_FILE += ".txt";
	                }
	                MEMORY = getMemory(TEXT_FILE);
	                PC = 0; //resets PC
	                System.out.println();
	                printState();
	                pause();
	                break;
	                
	            case "2": //Step
	                break;
	                
	            case "3": //Run
                    RUN = true;
	                break;
                    
	            case "4": //Changes the Memory Dump to start at a new chosen hex address
	                System.out.print("Enter starting memory address: ");
                    String memStartInput = inputReader.nextLine().trim();
                    int xIndex = memStartInput.indexOf('x'); //finds the x colon (begininng of hex number)
                    memStartInput = memStartInput.substring(xIndex + 1); //cuts off before x to get just the hex number
                    //Checks input line to see if they entered non-hex digits
                    if (!memStartInput.matches("^[0-9A-Fa-f]+$")) { 
                        System.out.println("\"" + memStartInput + "\" has invalid hex digits!");
                        pause();
                    //If user inputs more than 8 digits, warn that it is too long.
                    } else if (memStartInput.length() > 8) { 
                        System.out.println(memStartInput + " is too long!");
                        pause();
                    //If user inputs address that will dump out of bounds memory
                    } else if (Long.parseLong(memStartInput, 16) > (MEMORY.length - 16)) { 
                        System.out.println(memStartInput + " will cause memory out of bounds!");
                        pause();
                    } else { //Valid user input
                        MEMORY_DUMP_START = Long.parseLong(memStartInput, 16); //Parse Hex String input into a BigInteger       
                    }
                    printState();
                    pause();
                    break;
                    
	            case "5": //Save selected memory to a file
                    long startAddressIndex = -1; //Condition to know it has not been set
                    long endAddressIndex = -1; //Condition to know it has not been set
                    System.out.print("Enter name of file to save (Note: Lead with src/ in Eclipse): ");
                    String saveFile = inputReader.nextLine().trim();
                    //if file does not end in .txt, put .txt on the end
                    if (!saveFile.toLowerCase().endsWith(".txt")) { 
                        saveFile += ".txt";
                    }
                    System.out.print("Enter starting address: ");
                    String startAddress = inputReader.nextLine().trim();
                    xIndex = startAddress.indexOf('x'); //finds the x colon (begininng of hex number)
                    startAddress = startAddress.substring(xIndex + 1); //cuts off before x to get just the hex number
                    //Checks input line to see if they entered non-hex digits
                    if (!startAddress.matches("^[0-9A-Fa-f]+$")) { 
                        System.out.println("\"" + startAddress + "\" has invalid hex digits!");
                        pause();
                    //If user inputs more than 8 digits, warn that it is too long.
                    } else if (startAddress.length() > 8) { 
                        System.out.println(startAddress + " is too long!");
                        pause();
                    //If user inputs address that will get out of bounds memory
                    } else if (Long.parseLong(startAddress, 16) > (MEMORY.length - 1)) { 
                        System.out.println("0x"+startAddress + " will cause memory out of bounds!");
                        pause();
                    } else { //Valid user input
                        startAddressIndex = Long.parseLong(startAddress, 16); //Parse Hex String input into a BigInteger       
                    } if (startAddressIndex > 0) {//if start address is valid, continue
                        System.out.print("Enter ending address: ");
                        String endAddress = inputReader.nextLine().trim();
                        xIndex = endAddress.indexOf('x'); //finds the x colon (begininng of hex number)
                        endAddress = endAddress.substring(xIndex + 1); //cuts off before x to get just the hex number
                        //Checks input line to see if they entered non-hex digits
                        if (!endAddress.matches("^[0-9A-Fa-f]+$")) { 
                            System.out.println("\"" + endAddress + "\" has invalid hex digits!");
                            pause();
                        //If user inputs more than 8 digits, warn that it is too long.
                        } else if (endAddress.length() > 8) { 
                            System.out.println(endAddress + " is too long!");
                            pause();
                        //If user inputs address that will get out of bounds memory
                        } else if (Long.parseLong(endAddress, 16) > (MEMORY.length - 1)) { 
                            System.out.println("0x"+endAddress + " will cause memory out of bounds!");
                            pause();
                        //If user input end address is before beginning address
                        } else if (Long.parseLong(endAddress, 16) < startAddressIndex) { 
                            System.out.println("Ending address is before starting address!");
                            pause();
                        } else { //Valid user input
                            endAddressIndex = Long.parseLong(endAddress, 16); //Parse Hex String input into a BigInteger
                            saveMemory(saveFile, startAddressIndex, endAddressIndex);   
                        }
                    }
                    printState();
                    pause();
                    break;
                    
	            case "6": //Save selected memory to a file
	                System.out.print("Enter memory address to edit: ");
                    String memAddress = inputReader.nextLine().trim();
                    xIndex = memAddress.indexOf('x'); //finds the x colon (begininng of hex number)
                    memAddress = memAddress.substring(xIndex + 1); //cuts off before x to get just the hex number
                    //Checks input line to see if they entered non-hex digits
                    if (!memAddress.matches("^[0-9A-Fa-f]+$")) { 
                        System.out.println("\"" + memAddress + "\" has invalid hex digits!");
                        pause();
                    //If user inputs more than 8 digits, warn that it is too long.
                    } else if (memAddress.length() > 8) { 
                        System.out.println(memAddress + " is too long!");
                        pause();
                    //If user inputs address that will dump out of bounds memory
                    } else if (Long.parseLong(memAddress, 16) > (MEMORY.length - 1)) { 
                        System.out.println(memAddress + " will cause memory out of bounds!");
                        pause();
                    } else { //Valid user address
                        System.out.print("Enter new hex data: ");
                        String memData = inputReader.nextLine().trim();
                        xIndex = memData.indexOf('x'); //finds the x colon (begininng of hex number)
                        memData = memData.substring(xIndex + 1); //cuts off before x to get just the hex number
                        if (!memData.matches("^[0-9A-Fa-f]+$")) { 
                            System.out.println("\"" + memData + "\" has invalid hex digits!");
                            pause();
                        //If user inputs more than 8 digits, warn that it is too long.
                        } else if (memData.length() > 8) { 
                            System.out.println(memData + " is too long!");
                            pause();
                        //If user inputs address that will dump out of bounds memory
                        } else {
                            //Change data at address to new inputs
                            int memAddressInt = (int) Long.parseLong(memAddress, 16);
                            MEMORY[memAddressInt] = Long.parseLong(memData, 16);
                            //If edited address is less than 4, start mem dump at 0
                            if (memAddressInt < 4) {
                                MEMORY_DUMP_START = 0;
                            } else {
                                //Change memory dump start to 4 before the edited address
                                MEMORY_DUMP_START = memAddressInt - 4;   
                            }
                        }        
                    }
                    printState();
                    pause();
                    break;                
	                
	            case "9": //Terminate
	                state = HALT;
	                System.out.println("Terminating Console...");
	                break;
	                
	            default: //Invalid input
	                System.out.println("Please enter a valid command!");
	                pause();
	                break;
	        }	        
	    }  
	    catch(Exception e){ //Exception invalid file name
	        System.out.println("Error, invalid input!");
	        pause();
	    }	    
	}
    
    /**
     * Saves the system's memory from MEMORY[] into a text file from starting 
     * address to ending address.
     * 
     * @param saveFile - filename of text file to create
     * @param startAddressIndex - beginning address of memory block to copy
     * @param endAddressIndex - end address of memory block to copy
     */
    private void saveMemory(String saveFile, long startAddressIndex, long endAddressIndex) {
        try {
            PrintWriter out = new PrintWriter(new PrintStream(saveFile));
            for (long i = startAddressIndex; i <= endAddressIndex; i++) {
                //Output to file in format hex address:hex instruction (eg "A:E800000")
                out.println(Long.toHexString(i).toUpperCase()+":"+longToHex(MEMORY[(int) i]));
            }
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }   
    }

    /**
     * Returns a String "hex string" from the input long value.
     * 
     * @param input - long to convert
     * @return hex string that represents the input long
     */
    private String longToHex(long input) {
        //convert to uppercase hex
        String result = Long.toHexString(input).toUpperCase(); 
        //if more than 8 hex for some reason, cut off left hex digits
        while (result.length() > 8) {
            result = result.substring(1);
        }
        //if input is too small, pad 0's
        while (result.length() < 8) {
            result = "0" + result;
        }
        return result;
    }

    /**
     * Iterates timer, polls screen and keyboard, iterates keyboard stall.
     */
    private void checkIO() {
        if (timerCount == 0) { //if time reached 0, set timerCountComplete to true
            timerCountComplete = true;
            setTimerRegisters();
        }
        if (timerCountComplete && timerResetMode) { //if time reaches 0 and reset mode is on, reset it
            timerCount = getTimeCounter();
            timerCountComplete = false;
            setTimerRegisters();
        }
        if (timerCountComplete == false) { //if not 0, decrement
            timerCount--; //decrement the timer's count
        }
        if (!scrnReady) { //if scrn is not ready, send scrnData to screen
            setChanged();
            notifyObservers(scrnData);
        }        
        if (!kbdReady && scrnReady && kbdStall == 0) { //if kbd has data and scrn is ready, copy data to scrn
            scrnData = kbdData;
            kbdData = 0;
            kbdReady = true;
            scrnReady = false;
            setKBDRegisters();
            setSCRNRegisters();
            kbdStall = KBD_STALL;
        }
        if (!kbdReady && kbdStall > 0) { //stall keyboard
            kbdStall--;
        }
    }
    
    /**
     * Sets the time counter's default starting value - to the long input.
     * It stores the four 8-bit registers in the first word of IOMEMORY[].
     * 
     * @param input - long of timer count's reset value.
     */
    private void setTimeCounterResetValue(long input) {
        IOMEMORY[TIMER0] = input;
    }
    
    /**
     * Returns the time counter's starting value as a long.
     * 
     * @return the timer's reset value.
     */
    private long getTimeCounter() {
        return IOMEMORY[TIMER0];
    }
    
    /**
     * Sets the timer's program and status registers based on current settings.
     * They are stored in the same word (IOBASE+TIMER0+1).
     */
    private void setTimerRegisters() {
        long result = 0;
        if (timerInterruptMode) { //bit 1 of 4th byte (1st bit of word)
            result += 1;           
        }
        if (timerResetMode) { //bit 2 of of 4th byte (2nd bit of word)
            result += 2;           
        }
        if (timerCountComplete) { //bit 1 of 5th byte (9th bit of word)
            result += 256;
        }
        IOMEMORY[TIMER0+1] = result;
    }
    
    /**
     * Sets the boolean timerInterruptMode, timerResetMode, timerCountComplete based on the
     * bits stored in memory at IOBASE+TIMER0+1.
     */
    private void getTimerRegisters() {
        long word = IOMEMORY[TIMER0+1];
        String bin_word = longToBinaryString(word);
        if (bin_word.charAt(31) == 1) { //right most bit
            timerInterruptMode = true;
        } else {
            timerInterruptMode = false;
        }
        if (bin_word.charAt(30) == 1) { //2nd right most bit
            timerResetMode = true;
        } else {
            timerResetMode = false;
        }
        if (bin_word.charAt(23) == 1) { //9th bit from right
            timerCountComplete = true;
        } else {
            timerCountComplete = false;
        }       
    }
    
    /**
     * Sets the KBD's program and status registers based on current settings.
     * They are stored in the same word (IOBASE+KBD).
     */
    private void setKBDRegisters() {
        long result = 0;
        if (kbdReady) { //bit 1 of 1st byte (1st bit of word)
            result += 1;            
        }
        result += (kbdData * 256); //add KBD data to 2nd byte (9-16 bits of word)
        if (kbdInterruptMode) { //bit 1 of 3rd byte (17th bit of word)
            result += 65536;
        }
        IOMEMORY[KBD] = result;
    }
    
    /**
     * Sets the boolean kbdReady, char kbdData, boolean kbdInterruptMode based on the
     * bits stored in memory at IOBASE+KBD.
     */
    private void getKBDRegisters() {
        long word = IOMEMORY[KBD];
        String bin_word = longToBinaryString(word);
        if (bin_word.charAt(31) == 1) { //right most bit (1st bit of 1st byte)
            kbdReady = true;
        } else {
            kbdReady = false;
        }
        //get kbdData from 2nd byte of the word
        kbdData = (char) Integer.parseInt(bin_word.substring(16, 24), 2);
        if (bin_word.charAt(15) == 1) { //17th bit from right
            kbdInterruptMode = true;
        } else {
            kbdInterruptMode = false;
        }     
    }
    
    /**
     * Sets the SCRN's program and status registers based on current settings.
     * They are stored in the same word (IOBASE+SCRN).
     */
    private void setSCRNRegisters() {
        long result = 0;
        if (scrnReady) { //bit 1 of 1st byte (1st bit of word)
            result += 1;
            //add SCRN data to 2nd byte if SCRN ready is on
            result += (scrnData * 256); //bit 1-8 of 2nd byte (9-16 bits of word)
        }
        if (scrnInterruptMode) { //bit 1 of 3rd byte (17th bit of word)
            result += 65536;
        }
        IOMEMORY[SCRN] = result;
    }
    
    /**
     * Sets the boolean scrnReady, char scrnData, boolean scrnInterruptMode based on the
     * bits stored in memory at IOBASE+SCRN.
     */
    private void getSCRNRegisters() {
        long word = IOMEMORY[SCRN];
        String bin_word = longToBinaryString(word);
        if (bin_word.charAt(31) == 1) { //right most bit (1st bit of 1st byte)
            scrnReady = true;
        } else {
            scrnReady = false;
        }
        //get scrnData from 2nd byte of the word
        scrnData = (char) Integer.parseInt(bin_word.substring(16, 24), 2);
        if (bin_word.charAt(15) == 1) { //17th bit from right
            scrnInterruptMode = true;
        } else {
            scrnInterruptMode = false;
        }     
    }

    /**
     * Update the kbdData with char from SCRN_KBD or scrnReady with boolean.
     * 
     * @param obs - the observable object
     * @param obj - the object passed in the notifyObservers method
     */
    @Override
    public void update(Observable obs, Object obj) {
        if (obj.getClass().equals(Character.class)) { //if a character is passed
            if (kbdReady) {
                kbdData = (char) obj;
                kbdReady = false;
                setKBDRegisters();
            }
        } else if (obj.toString().equals("scrnReady")){ //if String scrnReady is passed
            scrnReady = true;
            scrnData = 0;
            setSCRNRegisters();
        }
    }
}