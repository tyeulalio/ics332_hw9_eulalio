package vmmanager;


import vmsimulation.*;

public class VirtualMemoryManagerV2 {

	MainMemory memory;
	BackingStore disk;
	Integer pageSize = 0;
	Integer numOfPages = 0;
	PageTable pageTable;
	int pageFaultCount = 0;
	int transferedByteCount = 0;
	int memoryPageSpace = 0;
	
	int addressSize = 4;

	// Constructor
	public VirtualMemoryManagerV2(MainMemory memory, BackingStore disk, Integer pageSize) throws MemoryException {
		// TO IMPLEMENT: V0, V1, V2, V3, V4
		this.memory = memory;
		
		// BackingStore
		this.disk = disk;
		this.pageSize = pageSize;
		numOfPages = disk.size() / pageSize;
		pageTable = new PageTable(numOfPages);
		memoryPageSpace = memory.size() / pageSize; // how many pages can fit in memory
	}
  
  	/**
  	 * Writes a value to a byte at a given address - 
  	 * To be implemented in versions V0 and above.
  	 * 
  	 * @throws MemoryException If there is an invalid access
  	 */
   	public void writeByte(Integer fiveByteBinaryString, Byte value) throws MemoryException {
		// TO IMPLEMENT: V0, V1, V2, V3, V4
   		int pageNumber = getPageNumber(fiveByteBinaryString);
   		
   		// check pageTable to see if page is in memory or only on disk
   		int memoryIndex = getMemoryIndex(pageNumber, fiveByteBinaryString);
   		// if it's in memory, then write to it
   		// if it's not, then the data needs to be pulled from disk
   			
   		// write value to the location
   		memory.writeByte(memoryIndex, value);
   		
   		// get the binary string for the fourByteBinaryString
   		String locationBits = BitwiseToolbox.getBitString(fiveByteBinaryString, addressSize);
   		
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
   	public Byte readByte(Integer fiveByteAddressString) throws MemoryException { 		// TO IMPLEMENT: V0, V1, V2, V3, V4
   		int pageNumber = getPageNumber(fiveByteAddressString);
   		byte value = 0;
   		String addressString = "";
   		
   		
   		// call getMemoryIndex
   		int memoryIndex = getMemoryIndex(pageNumber, fiveByteAddressString);
   		
		value = memory.readByte(memoryIndex);	// read location in memory	

		// get the string of the address using BitwiseToolbox for printing output
		addressString = BitwiseToolbox.getBitString(fiveByteAddressString, addressSize);
		
		// print the output message
		System.out.println("RAM read: " + addressString + " --> " + value);
  		return value;
  	}
   	
   	/**
   	 * returns the page number, given a virtual memory index
   	 */
   	public int getPageNumber(Integer fiveByteAddressString){
   		// pull the pageNumber from the integer
   		int pageNumber = fiveByteAddressString / pageSize;
   		return pageNumber;
   	}
   	
   	/**
   	 * Checks the pageTable to see if the page is stored in memory
   	 * if it's not, then it brings it into memory
   	 * Method returns the index of the element in main memory
   	 * @throws MemoryException 
   	 **/
   	public int getMemoryIndex(int pageNumber, int fiveByteAddressString) throws MemoryException{
   		int memoryIndex;
   		
   		// check page table to see if page is in memory 		
   		int pageLookUp = pageTable.lookUp(pageNumber);
   		// if it is, then read it
   		
   		if (pageLookUp >= 0){
   			// we don't have to do anything because the data is in memory
   			System.out.println("Page " + pageNumber + " is in memory");
   		}
   		
   		// if it's not, then grab it from disk and write it to memory in next open frame
   		else {
   			pageFaultCount++;
   			
   			pageLookUp = -pageLookUp;	// get rid of the negative that was returned
   			// catch the zero case
   			if (pageLookUp > numOfPages) pageLookUp = 0;
  
   			int nextOpenFrame = pageLookUp;	// assign the value to the next open frame
   			
   			System.out.println("Bringing page " + pageNumber + " into frame " + nextOpenFrame);

   			diskToMemory(pageNumber, nextOpenFrame * pageSize); // move page from disk to Memory
   			// update the page table
   			pageTable.update(pageNumber, nextOpenFrame);
   		}
   		
   		// calculate the index in memory
		int frameOffset = pageLookUp * pageSize;	// this is the offset of the address in physical memory
		int pageIndex = BitwiseToolbox.extractBits(fiveByteAddressString, 0, 3) % pageSize; // offset within a page/frame
   		memoryIndex = frameOffset + pageIndex;
		
   		return memoryIndex;
   	}
   	
   	
   	/**
   	 * Write data from disk to memory
   	 * @throws MemoryException 
   	**/
   	public void diskToMemory(int pageNumber, int memoryAddress) throws MemoryException{
   		byte[] pageValues = disk.readPage(pageNumber);
   		byte value;
   		
   		for (int i = 0; i < pageSize; i++){
   			value = pageValues[i];
   			memory.writeByte(memoryAddress, value);
   			transferedByteCount++;
   			memoryAddress++;
   		}
   		
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
   		byte[] pageContent; // holds values in each page of disk
   		
   		for (int k = 0; k < numOfPages; k++){ 			// loop through the pages on disk
   			System.out.print("PAGE " + k + ": ");		// print page number
   			pageContent = disk.readPage(k);
   			for (int h = 0; h < pageSize; h++){			// loop through array holding page contents
   				System.out.print(pageContent[h]); // print values in page
   				if (((h+1) % pageSize) != 0)
   					System.out.print(", ");
   			}
   			System.out.println();
   		}
  		return;
  	}
  	
  	/**
  	 * Writes all pages currently in memory frames back to disk - 
  	 * To be implemented in versions V1 and above.
  	 * 
  	 * @throws MemoryException If there is an invalid access
  	 */
  	public void writeBackAllPagesToDisk() throws MemoryException {
  		printMemoryContent();
  		// TO IMPLEMENT: V1, V2, V3, V4
  		// use the PageTable to make sure that you write the memory back to disk in the correct order
  		int frameIndex = 0;
  		byte[] pageElements = new byte[pageSize];
  		
  		// iterate through pages 0-numOfPages
  		for (int pageNumber = 0; pageNumber < numOfPages; pageNumber++){
  			// look at pageTable to find out where the page is stored in physical memory
  			if (pageTable.isValid(pageNumber) == 1){ // if page is in physical memory, then overwrite disk
  				frameIndex = getMemoryIndex(pageNumber, 0); // 0 because we're always looking for first element of the page

	  			// populate an array with pageSize elements from page
	  			for (int pageOffset = 0; pageOffset < pageSize; pageOffset++){
	  				pageElements[pageOffset] = memory.readByte(frameIndex + pageOffset);
	  			}
	  			// use BackingStore.writePage() to write the array back to the disk in the appropriate order
	  			disk.writePage(pageNumber, pageElements);
	  			transferedByteCount += pageSize;
  			}
  			// else, don't do anything to the disk
  		}
  	}
  	
  	/**
  	 * Returns the number of page faults that have occurred to date - 
  	 * To be implemented in versions V1 and above.
  	 * 
  	 * @return Number of page faults
  	 */
  	public int getPageFaultCount() {
  		// TO IMPLEMENT: V1, V2, V3, V4
		// increment this whenever the page searched for is not in main memory	
		return pageFaultCount;
  	}
  	
  	/**
  	 * Returns the number of bytes that have been transfered back and forth between the memory
  	 * and the disk to date -
  	 * To be implemented in versions V1 and above.
  	 * 
  	 * @return Number of bytes transferred
  	 */
  	public int getTransferedByteCount() {
  		// TO IMPLEMENT: V1, V2, V3, V4
  		return transferedByteCount;
  	}
}