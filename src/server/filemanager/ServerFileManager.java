/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.filemanager;

import client.networking.SyncFilesClientThread;
import client.objects.activedata.ActiveData;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.objects.matchdata.MatchData;
import utils.Flag;
import utils.Utils;

/**
 *
 * @author Kyle
 */
public class ServerFileManager
{

    //singleton object
    private static ServerFileManager matchManager = null;    

    private Flag blockAddingFilesToSendFlag = new Flag(false);

    private static final String ACTIVE_FOLDER_PATH = "./serverSaves/active";
    private static final String MATCH_FOLDER_PATH = "./serverSaves/match";
        

    private File matchFolder = null;    
    private File activeFolder = null;

    /**
     * Gets the singleton instance of this class.
     *
     * @return
     */
    public static ServerFileManager getInstance()
    {
        if (matchManager == null)
        {
            matchManager = new ServerFileManager();
        }
        return matchManager;
    }

    private ServerFileManager()
    {
        activeFolder = new File(ACTIVE_FOLDER_PATH);
        matchFolder = new File(MATCH_FOLDER_PATH);                

        //create local folder, if doesn't exist
        if (!matchFolder.exists())
        {
            matchFolder.mkdirs();
        }            
        //create local folder, if doesn't exist
        if (!activeFolder.exists())
        {
            activeFolder.mkdirs();
        }       
        //leave unlocked, should be locked for pulling all files from the SyncFilesThread only
        blockAddingFilesToSendFlag.unlock();
    }

    /**
     * Adds a completed match to the queue.
     *
     * @param match
     */
    public void addMatch(MatchData match)
    {
        //block until clear to add match data
        blockAddingFilesToSendFlag.await();
        FileWriter fw = null;
        String fileName = getFileNameFromMatchData(match);
        File localFile = new File(matchFolder, fileName);
        String content = match.serialize();
        try
        {
            System.out.println("Writing Server File: " + localFile.getCanonicalPath() + "\n"
                    + "Content: " + content);
            fw = new FileWriter(localFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServerFileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (fw != null)
                {
                    fw.close();
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(ServerFileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    /**
     * Adds a completed match to the queue.
     * 
     * @param active 
     */
    public void addActive(ActiveData active)
    {
        //block until clear to add match data
        blockAddingFilesToSendFlag.await();
        FileWriter fw = null;
        String fileName = getFileNameFromActiveData(active);
        File localFile = new File(activeFolder, fileName);
        String content = active.serialize();
        try
        {
            System.out.println("Writing File: " + localFile.getCanonicalPath() + "\n"
                    + "Content: " + content);
            fw = new FileWriter(localFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(ServerFileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                if (fw != null)
                {
                    fw.close();
                }
            }
            catch (IOException ex)
            {
                Logger.getLogger(ServerFileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }

    /**
     * Gets an File array from the local directory that need to be sent to the
     * server.
     * <br/>
     * Locks the folder so that the files can be fetched without modification.
     *
     * @return
     */
    public File[] getFilesToSend()
    {
        this.blockAddingFilesToSendFlag.lock();
        File[] matchFiles = matchFolder.listFiles();
        File[] activeFiles = activeFolder.listFiles();        
        this.blockAddingFilesToSendFlag.unlock();
        File[] files = Utils.appendFileArrays(matchFiles, activeFiles);
        return files;
    }

    /**
     * Retrieves the String for the file name from the Match Data object.
     *
     * @param match
     * @return
     */
    public static String getFileNameFromMatchData(MatchData match)
    {
        return String.valueOf(match.getMatchMatchNumber()) + "_"
                + String.valueOf(match.getMatchTeamNumber() + "_"
                        + String.valueOf(match.getMatchScouter().trim().replaceAll(" ", "_").replace("\\", "").replace("/", "").replace(".", ""))
                        + "_" + String.valueOf(match.serialize().hashCode()) + ".json");
    }
    
    /**
     * Retrieves the String for the file name from the Match Data object.
     *
     * @param match
     * @return
     */
    public static String getFileNameFromActiveData(ActiveData active)
    {
        return String.valueOf(String.valueOf(active.getMatchTeamNumber() + "_"
                        + String.valueOf(active.getMatchRobotScouter().trim().replaceAll(" ", "_").replace("\\", "").replace("/", "").replace(".", ""))
                        + "_" + String.valueOf(active.serialize().hashCode()) + ".json"));
    }
}