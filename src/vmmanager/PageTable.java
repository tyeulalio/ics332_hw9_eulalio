package vmmanager;

import java.util.LinkedList;
import java.util.Queue;

public class PageTable {
	private int[][] pageTable;
	private int[] frameTable;
	private int nextFrame = 0;
	private int numOfPages = 0;
	private int maxMemoryFrames = 0;
	Queue<Integer> freeFrames = new LinkedList<Integer>(); 
	private boolean pageTableFull = false;
	private boolean LRU = false;
	private int[] lruFrame;
	
	// constructor
	PageTable(Integer numOfPages){ // used when memory size is larger than disk
		this.numOfPages = numOfPages;
		pageTable = new int[numOfPages][3]; // pageTable[physical memory frame] [valid bit][dirty bit] for each page
		for (int i = 0; i < numOfPages; i++){		// set all of the valid bits to invalid (0) initially
			pageTable[i][1] = 0;	// valid bit
			pageTable[i][2] = 0;	// dirty bit
		}
	}

	PageTable(Integer numOfPages, int numOfFrames){
		this.numOfPages = numOfPages;
		maxMemoryFrames = numOfFrames;
		pageTable = new int[numOfPages][3]; // pageTable[physical memory frame] [valid bit] for each page
		for (int i = 0; i < numOfPages; i++){		// set all of the valid bits to invalid (0) initially
			pageTable[i][1] = 0;	// valid bit
			pageTable[i][2] = 0;	// dirty bit
		}
		//System.out.println("maxMemFrames: " + maxMemoryFrames);
		for (int i = 0; i < maxMemoryFrames; i++){
			freeFrames.add(i);
		}
		//System.out.println("right before: " + maxMemoryFrames);
		frameTable = new int[maxMemoryFrames];	// used to keep track of which page is in which frame
		
		if (LRU){
			lruFrame = new int[numOfFrames];
			for (int i = 0; i < numOfFrames; i++){
				lruFrame[i] = 0;	// set all frames to 0
			}
		}
	}
	
	public void queueFrame(int pageNumber){
		freeFrames.add(pageNumber);
	}
	
	public void setMaxMemoryFrames(int maxFrames){
		maxMemoryFrames = maxFrames;
	}
	
	public void setDirtyBit(int pageNumber){
		pageTable[pageNumber][2] = 1;	// sets dirty bit to 1
	}
	
	public void setLRU(){
		LRU = true;
	}
	
	public void resetLruTable(int frameNumber){
		lruFrame[frameNumber] = 0;
	}
	
	public boolean getPageTableFull(){
		return pageTableFull;
	}
	
	public int getPageInFrame(int frameNumber){
		return frameTable[frameNumber];
	}
	
	public void updateFrame(int frameNumber){
		frameTable[frameNumber] = -1;
	}
	
	public int getDirtyBit(int pageNumber){
		return pageTable[pageNumber][2];
	}
	
	// updates the page table
	public void update(int pageNumber, int frameNumber){
		pageTable[pageNumber][1] = 1;			// sets valid bit once page is written to memory
		pageTable[pageNumber][0] = frameNumber;	// writes the memory frame number to the page's row
		//System.out.println("frameNumber: " + frameNumber + " pageNumber: " + pageNumber);
		//System.out.println("maxMemoryFrames: " + maxMemoryFrames);
		if (maxMemoryFrames != 0)	frameTable[frameNumber] = pageNumber; 	// store in frameTables the pageNumber stored there
   		//System.out.println("TESTTESTTEST");
		nextFrame++;
	}
	
	public int lookUp(int pageNumber){
		// check pageTable to see if pageNumber is stored in physical memory
		// if it is, then return the frame number of physical memory where it's stored
		if (isValid(pageNumber) == 1){
			int frame = pageTable[pageNumber][0];
			
			
			return frame;
		}
		
		// if not, then page fault is thrown		
		else {
			if (nextFrame == 0) {
				return -(numOfPages+1); // this catches the zero case because 0 can't be negative
			// return -(next available frame number)
			// then the virtual memory manager can write into that frame	
			}
			else {
				// check if the next frame exceeds the memory content of the physical memory
				if (nextFrame >= maxMemoryFrames){
					pageTableFull = true;
					// need to put things into a queue
					if (maxMemoryFrames != 0){ // this implements the FCFS frame 
						int clearedFrame = freeFrames.remove();
						Integer evictedPage = frameTable[clearedFrame];
						pageTable[evictedPage][1] = 0;	// page is no longer valid because it's evicted
						//pageTable[evictedPage][2] = 0;	// page is no longer dirty because it's not in memory
						if (clearedFrame == 0) return -(numOfPages+1); // needs to catch zero case like earlier
						else return -clearedFrame;
					}
				}
				return -nextFrame;
			}
		}
	}
	
	public int isValid(int pageNumber){
		// keep track of which pages are in memory and which are on disk
		// valid = legal and in memory
		// invalid = illegal or on disk
		// initially set to invalid for all entries
		// during address translation, if the bit is invalid a trap is generated: a page fault
		return pageTable[pageNumber][1];
	}
}
