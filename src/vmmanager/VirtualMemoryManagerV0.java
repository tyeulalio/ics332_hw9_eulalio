package vmmanager;


import vmsimulation.*;

public class VirtualMemoryManagerV0 {

	MainMemory memory;
	int addressSize = 3;

	// Constructor
	public VirtualMemoryManagerV0(MainMemory memory, BackingStore disk, Integer pageSize) throws MemoryException {
		// TO IMPLEMENT: V0, V1, V2, V3, V4
		this.memory = memory;
	}
  
  	/**
  	 * Writes a value to a byte at a given address - 
  	 * To be implemented in versions V0 and above.
  	 * 
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public void writeByte(Integer fourByteBinaryString, Byte value) throws MemoryException {
		// TO IMPLEMENT: V0, V1, V2, V3, V4
   			
   		// write value to the location
   		memory.writeByte(fourByteBinaryString, value);
   		
   		// get the binary string for the fourByteBinaryString
   		String locationBits = BitwiseToolbox.getBitString(fourByteBinaryString, addressSize);
   		
   		// print output message
   		System.out.println("RAM write: " + locationBits + " <-- " + value);
  		return;
  	}

  	/**
  	 * Reads a byte from a given address - 
  	 * To be implemented in versions V0 and above.
  	 * 
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public Byte readByte(Integer fourByteBinaryString) throws MemoryException {
		byte value = 0;
		String addressString = "";
		
		// TO IMPLEMENT: V0, V1, V2, V3, V4
		
		// read the location in main memory
		value = memory.readByte(fourByteBinaryString);
		
		// get the string of the address using BitwiseToolbox
		addressString = BitwiseToolbox.getBitString(fourByteBinaryString, addressSize);
		
		// print the output message
		System.out.println("RAM read: " + addressString + " --> " + value);
  		return value;
  	}
  	
  	/**
  	 * Prints all memory content -
  	 * To be implemented in versions V0 and above.
  	 * 
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public void printMemoryContent() throws MemoryException {
   		String addressBits = "";
   		int value = 0;
   		for (int i = 0; i < memory.size(); i++){
   			addressBits = BitwiseToolbox.getBitString(i, addressSize); // used for printing address location
   			value = memory.readByte(i); // print value in address location
   			
   			System.out.println(addressBits + ": " + value);
   		}
   		
  	}
  	
  	/**
  	 * Prints all disk content, structured by pages -
  	 * To be implemented in versions V1 and above.
  	 * 
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public void printDiskContent() throws MemoryException {
  		// TO IMPLEMENT: V1, V2, V3, V4
  		return;
  	}
  	
  	/**
  	 * Writes all pages currently in memory frames back to disk - 
  	 * To be implemented in versions V1 and above.
  	 * 
  	 * @throws MemoryException If there is an invalid access
  	 */
  	public void writeBackAllPagesToDisk() throws MemoryException {
  		// TO IMPLEMENT: V1, V2, V3, V4
  	}
  	
  	/**
  	 * Returns the number of page faults that have occurred to date - 
  	 * To be implemented in versions V1 and above.
  	 * 
  	 * @return Number of page faults
  	 */
  	public int getPageFaultCount() {
		int count = 0;
  		// TO IMPLEMENT: V1, V2, V3, V4
  		return count;
  	}
  	
  	/**
  	 * Returns the number of bytes that have been transfered back and forth between the memory
  	 * and the disk to date -
  	 * To be implemented in versions V1 and above.
  	 * 
  	 * @return Number of bytes transferred
  	 */
  	public int getTransferedByteCount() {
		int count = 0;
  		// TO IMPLEMENT: V1, V2, V3, V4
  		return count;
  	}
}