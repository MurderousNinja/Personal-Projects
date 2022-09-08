import java.util.*;
import java.io.*;
import java.net.*;

class AnimeRename
{
    public static void main(String ar[])
    {
        try (Scanner s = new Scanner(System.in)) 
        {
            //Taking the directory path.
            System.out.println("Enter the directory path (With a slash at the end): ");
            String path = s.nextLine();

            //Taking the choice of the user.
            System.out.println();
            int choice = 1;

            //Creating an array with all the file names.
            File directoryPath = new File(path);
            String contents[] = directoryPath.list();

            //Making an object for the other class
            edit obj = new edit();

            //Making necessary arrays
            String episodelist[] = new String [1000];
            int episodecounter[] = new int [10];

            //Taking the website path.
            System.out.println("Go to this link and select the series you want to rename.");
            System.out.println("https://en.wikipedia.org/wiki/Category:Lists_of_anime_episodes");
            String url = s.nextLine();

            //Using this to get all the episode names.
            if(choice == 1)
            {
                try
                {
                    //Making a url to connect to internet and extract values.
                    URL Url = new URL(url);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(Url.openStream()));
                    String line;

                    //Making counter variables.
                    int count = 1;
                    int universalcounter = 0;
                    int counter = 0;

                    while ((line = reader.readLine()) != null)
                    {
                        String d1 = obj.season(line);

                        if(d1.contains("xadcs")){}
                        else
                        {
                            episodecounter[counter] = count - 1;
                            counter = counter + 1;
                            episodelist[universalcounter] = d1;
                            count = 1;
                            universalcounter ++;
                        }
                        if(line.contains("PV"))
                        {

                        }
                        else if(line.contains("<td class=\"summary\" style=\"text-align:left\">"))
                        {
                            episodelist[universalcounter] = obj.extraction(line,count);
                            count ++;
                            universalcounter ++;
                        }
                    }
                    episodecounter[counter] = count - 1;

                    reader.close();
                }   
                catch(Exception ex)
                {
                    System.out.println(ex);
                }
            }
            else if(choice == 2)
            {

            }
            else
            {
                System.out.println("Invalid Input");
                System.exit(0);
            }

            //Making a integer for number of files
            int files = contents.length;

            //Checking if path contains Season or not
            int season = 0,n,m;
            String seasonstr,series = "";
            if(path.contains("Season") || path.contains("season"))
            {
                //Getting in form Season 1
                seasonstr = obj.name(path);
                seasonstr = seasonstr.replaceAll("Season "," ");
                seasonstr = seasonstr.trim();

                //Taking season number
                season = Integer.parseInt(seasonstr);

                //Taking the series name
                n = m = 1;
                series = obj.namebefore(path, m, n);
            }

            //Taking the series name if it doesn't have season
            else
            {
                series = obj.name(path);
            }

            //Renaming Station.
            String filename = "";
            String renamed = "";
            String confirmation = "";
            int count = 0;

            //
            for (int i = 0; i < contents.length; i++ ) 
            {
            	//Taking the name one by one.
            	String temp = contents[i];

                //Taking the extention
                String extention = obj.extention(temp, count);
                String bareextention = extention.replace('.',' ');
                bareextention = bareextention.trim();

            	//Cleaning the String
            	String tempu = obj.clean(temp, bareextention, series, season);

            	//Making use of string tokenizer
            	//StringTokenizer str = new StringTokenizer(tempu);

            	//Adding series name to the renamed
            	renamed = renamed + series;

                //Adding season if it is in the path
                if(path.contains("Season"))
                {
                    //Adding season number.
                    if(season <10)
                    {
                        renamed = renamed + " S0" + String.valueOf(season);
                    }
                    else
                    {
                        renamed = renamed + " S" + String.valueOf(season);
                    }

                    int a = 0;
                    while(a == 0)
                    {
                        //Taking episode number.
            		    int numero = obj.episode(tempu,a);
                        
                        //Taking episode name.
                        String episodename = obj.Episode(episodelist, numero, season, episodecounter, choice);

                        if(numero>0 && numero <= files)
                        {
                            //Arranging everything in order.
                            renamed = renamed + "E";
                            if(numero<10)
                            {
                                renamed = renamed + "0" + String.valueOf(numero) + " - " + episodename;
                                a = a + 1;
                            }
                            else
                            {
                                renamed = renamed + String.valueOf(numero) + " - " + episodename;
                                a = a + 1;
                            }
                        }
                    }
                }

                //Not adding season and just continuing with episode if season is not in path
                else
                {
                    //Making numero for episode number.
                    int a = 0;

                    while(a == 0)
                    {
                        //Taking episode number.
            		    int numero = obj.episode(tempu,a);

                        //Taking episode name.
                        String episodename = obj.Episode(episodelist, numero, season, episodecounter, choice);
                        
                        if(numero>0 && numero <= files)
                        {
                            //Arranging everything in order.
                            if(contents.length > 100)
                            {
                                renamed = renamed + " Ep ";
                                if(numero<10)
                                {
                                    renamed = renamed + "00" + String.valueOf(numero) + " - " + episodename;
                                    a = a + 1;
                                }
                                else if(numero<100)
                                {
                                    renamed = renamed + "0" + String.valueOf(numero) + " - " + episodename;
                                    a = a + 1;
                                }
                                else
                                {
                                    renamed = renamed + String.valueOf(numero) + " - " + episodename;
                                    a = a + 1;
                                }
                            }
                            else
                            {
                                renamed = renamed + " Ep ";
                                if(numero<10)
                                {
                                    renamed = renamed + "0" + String.valueOf(numero) + " - " + episodename;
                                    a = a + 1;
                                }
                                else
                                {
                                    renamed = renamed + String.valueOf(numero) + " - " + episodename;
                                    a = a + 1;
                                }
                            }
                        }
                    
                    }
                }

                //Adding the extention
            	renamed = renamed + extention;
            	
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
            System.out.println(count);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }
}

//Other class to manage activities such as getting a series name or renaming.
class edit
{
	//Function for Taking out Series Name.
	public String name(String path)
	{
        int n = 0,m = 0;
        for(int i=0; i<path.length(); i++)
        {
            
            if(path.charAt(i) == '\\')
            {
                n = n + 1;
            }
        }

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

    //Function for Taking out the name before if path contains season
    public String namebefore(String path,int m,int n)
	{

        for(int i=0; i<path.length(); i++)
        {
            
            if(path.charAt(i) == '\\')
            {
                n = n + 1;
            }
        }

        int start = 0,end = 0;
        for(int i=0; i<path.length(); i++)
        {
            if(path.charAt(i) == '\\')
            {
                m = m + 1;
                if(m == n - 2)
                {
                    start = i + 1;
                }
                else if(m == n - 1)
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
	public String clean(String name, String extention, String b, int season)
	{
		//Cleaning _
		while(name.contains("_"))
		{
			name = name.replace('_',' ');
		}

		//Cleaning -
		while(name.contains("-"))
		{
			name = name.replace('-',' ');
		}

		//Cleaning .
		while(name.contains("."))
		{
			name = name.replace('.',' ');
		}

		//Cleaning the extention
		while(name.contains(extention))
		{
			name = name.replaceAll(extention," ");
		}
		
		//Removing ()
		if(name.contains("(") || name.contains(")"))
		{
			name = name.replace("(", " ");
			name = name.replace(")", " ");
		}

        //Changing ep00 to ep 00
        if(name.contains("Ep") || name.contains("ep") || name.contains("EP") || name.contains("E"))
        {
            name = name.replaceAll("Ep", "Ep ");
            name = name.replaceAll("EP", "Ep ");
            name = name.replaceAll("ep", "Ep ");
            name = name.replaceAll("E", "E ");
        }

        int year = 2000;
        for(int i = 0; i <= 30; i++)
        {
            String years = String.valueOf(year);
            if(name.contains(years))
            {
                name = name.replaceAll(years," ");
            }
            year = year + 1;
        }

        String ab1 = "S0" + String.valueOf(season);
        String ab2 = "s0" + String.valueOf(season);
        String ab3 = "s" + String.valueOf(season);
        if(name.contains(ab1) || name.contains(ab2) || name.contains(ab3))
        {
            name = name.replaceAll(ab1, " ");
            name = name.replaceAll(ab2, " ");
            name = name.replaceAll(ab3, " ");
        }

		//Returning clean string.
		return name;
	}

    //Function to get extention.
    public String extention(String path, int b)
    {
        int n = 0,m = 0;
        for(int i=0; i<path.length(); i++)
        {
            
            if(path.charAt(i) == '.')
            {
                n = n + 1;
            }
        }

        int start = 0,end = path.length();
        for(int i=0; i<path.length(); i++)
        {
            if(path.charAt(i) == '.')
            {
                m = m + 1;
                if(m == n)
                {
                    start = i;
                }
            }
        }
        
        String a = path.substring(start, end);
        return a;
    }

    //Function to get the episode number.
    public int episode(String contentname, int counter)
    {
        int trial = 0;
        int c = 0;
        StringTokenizer str = new StringTokenizer(contentname);
        while(str.hasMoreTokens() && counter == 0)
        {
            try
            {
                String b = str.nextToken();
                c = Integer.parseInt(b);
                int d = c/0;
                d = d + 1;
            }
            catch(NumberFormatException E){}
            catch(ArithmeticException A)
            {
                trial = c;
                counter = counter + 1;
            }
        }
        return trial;
    }

    //Extracting Name of episodes.
    public String extraction(String line,int count)
    {
        StringTokenizer s = new StringTokenizer(line,">");

        String b;

        while(s.hasMoreTokens())
        {
            b = s.nextToken();
            
            if(b.contains("<br") && b.length()> 8)
            {
                b = b.replaceAll("<br /", "");
                b = b.substring(1, b.length()-1);
                //b = String.valueOf(count) + " " + b;
                return b;
            }
        }
        String a = " ";
        return a;
    }
    
    //Taking output for season.
    public String season(String line)
    {
        StringTokenizer str = new StringTokenizer(line,">");

        String b;

        while(str.hasMoreTokens())
        {
            b = str.nextToken();
            
            if(b.contains("<span id="))
            {
                
                b = b.replaceAll("<span id=", " ");
                b = b.replace("_", " ");
                b = b.substring(2,11);
                b = b.trim();
                return b;
            }
        }
        
        String a = "xadcs";
        return a;
    }

    //
    public String Episode(String list[],int Epno,int season,int episodecounter[],int choice)
    {
        int episodenumber = 0;
        
        if(season == 1)
        {
            episodenumber = Epno + 1;
            return list[episodenumber];
        }
        else
        {
            for(int i = 1; i < season; i++)
            {
                episodenumber = episodenumber + episodecounter[i] + 1;
            }
            episodenumber = episodenumber + Epno;
            return list[episodenumber];
        }
    }
}