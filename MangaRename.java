import java.io.*;
import java.util.*;

//D:\Dare\Manga\The Last Human\

class MangaRename
{
	public static void main(String ar[])
	{
		try (//Setting up scanner.
		Scanner s = new Scanner(System.in)) {
			//Taking the directory path.
			System.out.println("Enter the directory path (With a slash at the end): ");
			String path = s.nextLine();
			
			//Creating an array with all the file names.
			File directoryPath = new File(path);
			String contents[] = directoryPath.list();

			//Taking the extention.
			String extention = "pdf";

			//Making objects for the classes.
			tools obj = new tools();

			//Using function to get manga name.
			String manga = obj.name(path);
			
			//Renaming Station.
			String filename = "";
			String renamed = "";
			String confirmation = "";
			int count = 0;

			for (int i = 0; i < contents.length; i++ ) 
			{
				//Taking the name one by one.
				String temp = contents[i];

				//Cleaning the String
				String tempu = obj.clean(temp, extention, manga);

				//Making use of string tokenizer
				StringTokenizer str = new StringTokenizer(tempu);

				//Adding manga name to the renamed
				renamed = renamed + manga;

				//Making numero for chapter number or volume number.
				String numero;
				
				//Making integers for loop ending
				int m = 0;
				int n = 0;

				//System.out.println(tempu);

				//For the template "Volume 000" or "Manga Name - Volume 000".
				if(tempu.contains("Volume"))
				{
					while(str.hasMoreTokens())
					{
						if(str.nextToken().equalsIgnoreCase("volume"))
						{
							//Taking the number as string.
							numero = str.nextToken();

							//Making the number into "01" format
							int num = Integer.parseInt(numero);
							if(num<10)
							{
								numero = "0" + String.valueOf(num);
							}

							//Adding numero to renamed
							renamed = renamed + " - Volume " + numero;

							//Ending the loop
							m = m + 1;
						}
					}
				}
				
				//For the template "Manga Name - Chapter 001" or "Chapter 001" or "Ch 001" or "Ch001" or "Episode 001".
				else if(tempu.contains("Chapter") || tempu.contains("Ch") || tempu.contains("Episode"))
				{
					while(n == 0)
					{
						String ste = str.nextToken();
						if(ste.equalsIgnoreCase("chapter") || ste.equalsIgnoreCase("ch") || ste.equalsIgnoreCase("Episode"))
						{
							//Taking the number as string.
							numero = str.nextToken();

							//Making the number into "001" format
							int num = Integer.parseInt(numero);
							if(num<10)
							{
								numero = "00" + String.valueOf(num);
							}
							else if(num<100)
							{
								numero = "0" + String.valueOf(num);
							}

							//Adding numero to renamed
							renamed = renamed + " - Chapter " + numero;

							//Ending the loop
							n = n + 1;
						}
					}
				}
				
				//For the template "Manga Name - 001" or "001" or 
				else
				{
					tempu = tempu.trim();
					StringTokenizer stri = new StringTokenizer(tempu);

					//Taking the number as string.
					numero = stri.nextToken();

					//Making the number into "001" format
					int num = Integer.parseInt(numero);
					if(num<10)
					{
						numero = "00" + String.valueOf(num);
					}
					else if(num<100)
					{
						numero = "0" + String.valueOf(num);
					}

					//Adding numero to renamed
					renamed = renamed + " - Chapter " + numero;
				}
				
				if(renamed.contains(extention))
				{
				}
				else
				{
					renamed = renamed + "." + extention;
				}
				
				
				if(temp.contains(extention))
				{
				}
				else
				{
					temp = temp + "." + extention;
				}
				
				//Setting up the filenames and file renames for renaming
				filename = path + temp;
				renamed = path + renamed;
				
				//Using user defined function to rename.
				confirmation = obj.renaming(filename,renamed);
				
				//Counting the number of sucesses.
				if(confirmation.equals("Yes"))
				{
					count = count + 1;
				}
				
				
				//Restting for further use.
				filename = "";
				renamed = "";

			}
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
	}
}

//Other class to manage activities such as getting a manga name or renaming.
class tools
{
	//Function for Taking out Manga Name.
	public String name(String path)
	{
		int n = 0;
        for(int i=0; i<path.length(); i++)
        {
            
            if(path.charAt(i) == '\\')
            {
                n = n + 1;
            }
        }

        int m = 0;
        int start = 0,end = 0;
        for(int i=0; i<path.length(); i++)
        {
            if(path.charAt(i) == '\\')
            {
                m = m + 1;
                if(m == n - 1)
                {
                    start = i + 1;
                }
                else if(m == n)
                {
                    end = i;
                }
            }
        }
        
        String a = path.substring(start, end);
        return a;
	}
	
	//Function for renaming
	public String renaming(String orignal, String renamed)
    {
	
		//Create an object of the File class
		File file = new File(orignal);
  
		//Create an object of the File class
		File rename = new File(renamed);
		
		//Store the return value of renameTo() method in flag
		boolean flag = file.renameTo(rename);
		
		if (flag == true)
		{
			return "Yes";
		}
		else
		{
			return "No";
		}
    }

	//Function to clean the string.
	public String clean(String c, String extention, String b)
	{
		//Cleaning _
		while(c.contains("_"))
		{
			c = c.replace('_',' ');
		}

		//Cleaning -
		while(c.contains("-"))
		{
			c = c.replace('-',' ');
		}

		//Cleaning .
		while(c.contains("."))
		{
			c = c.replace('.',' ');
		}

		//Making ch001 to ch 001
		if(c.contains("Chapter") || c.contains("chapter")){}
		else if(c.contains("Ch") || c.contains("ch"))
		{
			if(c.contains("ch"))
			{
				c = c.replaceAll("ch","Ch ");
			}
			else if(c.contains("Ch"))
			{
				c = c.replaceAll("Ch","Ch ");
			}
		}

		//Making v01 to v 01
		if(c.contains("Volume") || c.contains("V")){}
		/*else
		{
			if(c.contains("ch"))
			{
				c = c.replaceAll("ch","Ch ");
			}
			else if(c.contains("Ch"))
			{
				c = c.replaceAll("Ch","Ch ");
			}
		}*/


		//Removing Manga Name
		if(c.contains(b))
		{
			c = c.replaceAll(b, " ");
		}

		//Cleaning the extention
		while(c.contains(extention))
		{
			c = c.replaceAll(extention," ");
		}

		//Removing @mangamanwha
		if(c.contains("@mangamanwha"))
		{
			c = c.replaceAll("@mangamanwha"," ");
		}
		if(c.contains("@MangaManwha"))
		{
			c = c.replaceAll("@MangaManwha"," ");
		}
		
		//Removing ()
		if(c.contains("(") || c.contains(")"))
		{
			c = c.replace("(", " ");
			c = c.replace(")", " ");
		}

		//Returning clean string.
		return c;
	}
}