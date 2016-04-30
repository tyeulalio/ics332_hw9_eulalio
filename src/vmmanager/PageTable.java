package vmmanager;

public class PageTable {
	private int[][] pageTable;
	private int nextFrame = 0;
	private int numOfPages = 0;
	
	// constructor
	PageTable(int numOfPages){
		this.numOfPages = numOfPages;
		pageTable = new int[numOfPages][2]; // pageTable[physical memory frame] [valid bit] for each page
		for (int i = 0; i < numOfPages; i++){		// set all of the valid bits to invalid (0) initially
			pageTable[i][1] = 0;
		}
	}
	
	// updates the page table
	public void update(int pageNumber, int frameNumber){
		pageTable[pageNumber][1] = 1;			// sets valid bit once page is written to memory
		pageTable[pageNumber][0] = frameNumber;	// writes the memory frame number to the page's row
		nextFrame++;
		return;
	}
	
	public int lookUp(int pageNumber){
		// check pageTable to see if pageNumber is stored in physical memory
		// if it is, then return the frame number of physical memory where it's stored
		if (isValid(pageNumber) == 1){
			return pageTable[pageNumber][0];
		}
		
		// if not, then page fault is thrown		
		else {
			if (nextFrame == 0) return -(numOfPages+1); // this catches the zero case because 0 can't be negative
			// return -(next available frame number)
			// then the virtual memory manager can write into that frame
			else {
				// check if the next frame exceeds the memory content of the physical memory
				if (VirtualMemoryManagerV2.getMemoryPageMax() <= nextFrame){
					// 
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
