package cmd;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;
//Blank AFL Creator by ViveTheModder
public class Main 
{
	public static int getLittleEndianInt(int data)
	{
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.asIntBuffer().put(data);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getInt();
	}
	public static int getNumberOfDigits(int num)
	{
		int cnt=0;
		while (num!=0)
		{
			num/=10;
			cnt++;
		}
		return cnt;
	}
	public static void writeAFL(String aflName, int fileCnt) throws IOException
	{
		if (!aflName.endsWith(".afl")) aflName+=".afl";
		RandomAccessFile afl = new RandomAccessFile(aflName,"rw");
		long header1 = 0x41464C0001000000L;
		int header2 = 0xFFFFFFFF, myLength=0;
		//write header (first 16 bytes)
		afl.writeLong(header1);
		afl.writeInt(header2);
		afl.writeInt(getLittleEndianInt(fileCnt));
		myLength+=16;
		
		int fileCntDigits = getNumberOfDigits(fileCnt);
		int currentDigits=0, numberOfZeroes=0;
		for (int i=0; i<fileCnt; i++)
		{
			currentDigits = getNumberOfDigits(i);
			numberOfZeroes = fileCntDigits-currentDigits;
			String fileName = "blank_";
			for (int j=0; j<numberOfZeroes; j++)
				fileName+=0;
			if (i!=0) fileName+=i;
			afl.writeBytes(fileName);
			
			int fileNameLen = fileName.length();
			myLength+=fileNameLen;
			for (int k=fileNameLen; k<32; k++)
				afl.writeByte(0x0);
			myLength+=32-fileNameLen;
		}
		//this was added incase the user overwrites an existing AFL
		afl.setLength(myLength);
		afl.close();
		System.out.println(aflName+" complete!");
	}
	public static void main(String[] args) throws IOException
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the file name of the AFL (extension is added automatically): ");
		String aflName = sc.nextLine();
		System.out.print("Enter the number of files for the AFL: ");
		int fileCnt = sc.nextInt();
		//sc.next() methods don't support unsigned data types
		if (fileCnt<=0 || fileCnt>65535)
		{
			System.out.println("Incorrect number of files!"); System.exit(1);
		}
		sc.close();
		long start = System.currentTimeMillis();
		writeAFL(aflName,fileCnt);
		long finish = System.currentTimeMillis();
		long time = (finish-start)/1000;
		System.out.println("Time: "+time+" s");
	}
}