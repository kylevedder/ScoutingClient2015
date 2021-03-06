/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.frames;

import client.frames.MatchFrame;
import client.objects.activedata.ActiveData;
import client.objects.matchdata.MatchData;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableRowSorter;
import main.Globals;
import main.Main;
import server.filemanager.ServerFileManager;
import server.networking.SyncFilesServerThread;
import server.objects.MultiMatchData;
import utils.FileUtils;

/**
 *
 * @author kyle
 */
public class ServerFrame extends javax.swing.JFrame
{

    private SyncFilesServerThread syncFilesServerThread = null;

    private ServerFileManager serverFileManager = ServerFileManager.getInstance();
    public MatchViewerFrame matchViewerFrame = new MatchViewerFrame(null);
    public ActiveViewerFrame activeViewerFrame = new ActiveViewerFrame(null);
    public MultiMatchViewer multiMatchViewerFrame = new MultiMatchViewer();
    
    public HashMap<Integer, MultiMatchData> mapHash = null;

    /**
     * Creates new form ServerFrame
     */
    public ServerFrame()
    {
        initComponents();
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
            this.setLocationRelativeTo(null);
            this.pack();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            Logger.getLogger(MatchFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        populateTables();
    }

    /**
     * Pulls all data from the server saves files and populates the tables.
     */
    public void populateTables()
    {
        setupActiveTable();
        setupMatchTable();
        setupTeamViewer();
        this.matchViewerFrame.setVisible(false);
        this.activeViewerFrame.setVisible(false);
        this.multiMatchViewerFrame.setVisible(false);

    }

    /**
     * Sets up team viewer table.
     */
    private void setupTeamViewer()
    {
        System.out.println("Setting up team viewer");
        ArrayList<File> files = this.serverFileManager.getMatchFiles();
        mapHash = new HashMap<>();                        
        for (File f : files)
        {
            MatchData matchData = MatchData.deserialize(FileUtils.readFileContents(f));
            MultiMatchData multiMatchData = mapHash.get(matchData.getMatchTeamNumber());
            if(multiMatchData != null)
            {
                multiMatchData.addMatchData(matchData);
                mapHash.remove(matchData.getMatchTeamNumber());
                mapHash.put(matchData.getMatchTeamNumber(), multiMatchData);
            }
            else
            {
                multiMatchData = new MultiMatchData(null, matchData.getMatchTeamNumber());
                multiMatchData.addMatchData(matchData);
                mapHash.put(matchData.getMatchTeamNumber(), multiMatchData);
            }
        }
        System.out.println("Team viewer setup...");
        this.tableTeamViewer.setModel(new TeamViewerTableModel(mapHash));
        TableRowSorter mySorter = new TableRowSorter(tableTeamViewer.getModel());
        mySorter.setComparator(0, new NumericComparator());
        mySorter.setComparator(1, new NumericComparator());
        mySorter.setComparator(2, new NumericComparator());
        mySorter.setComparator(3, new NumericComparator());
        mySorter.setComparator(4, new NumericComparator());
        mySorter.setComparator(5, new NumericComparator());
        mySorter.setComparator(6, new NumericComparator());
        mySorter.setComparator(7, new NumericComparator());
        mySorter.setComparator(8, new NumericComparator());
        tableTeamViewer.setRowSorter(mySorter);
        tableTeamViewer.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 1)
                {
                    System.out.println("Click");
                    int teamNum = (Integer) tableTeamViewer.getValueAt(tableTeamViewer.getSelectedRow(), 0);
                    System.out.println("Team num: " + teamNum);
                    MultiMatchData multiMatchData = mapHash.get(teamNum);                    
                    multiMatchViewerFrame.loadData(multiMatchData);
                    multiMatchViewerFrame.setVisible(true);
                }
            }
        });
    }

    /**
     * Sets up the params of the table for display.
     */
    private void setupMatchTable()
    {

        ArrayList<File> matchFiles = this.serverFileManager.getMatchFiles();
        this.tableMatch.setModel(new MatchTableModel(matchFiles));
        TableRowSorter mySorter = new TableRowSorter(tableMatch.getModel());
        mySorter.setComparator(0, new NumericComparator());
        mySorter.setComparator(1, new NumericComparator());
        mySorter.setComparator(2, new StringComparator());
        mySorter.setComparator(3, new NumericComparator());
        mySorter.setComparator(4, new StringComparator());
        tableMatch.setRowSorter(mySorter);
        tableMatch.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 1)
                {
                    String fileName = (String) tableMatch.getValueAt(tableMatch.getSelectedRow(), 4);
                    System.out.println("File: " + fileName);
                    String fileContents = serverFileManager.readFile(fileName);
                    MatchData matchData = MatchData.deserialize(fileContents);
                    matchViewerFrame.setVisible(true);
                    matchViewerFrame.resetFrame(matchData);
                }
            }
        });
    }

    /**
     * Sets up the params of the table for display.
     */
    private void setupActiveTable()
    {
        ArrayList<File> activeFiles = this.serverFileManager.getActiveFiles();
        this.tableActive.setModel(new ActiveTableModel(activeFiles));
        TableRowSorter mySorter = new TableRowSorter(tableActive.getModel());
        mySorter.setComparator(0, new NumericComparator());
        mySorter.setComparator(1, new StringComparator());
        mySorter.setComparator(2, new StringComparator());
        tableActive.setRowSorter(mySorter);
        tableActive.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 1)
                {
                    String fileName = (String) tableActive.getValueAt(tableActive.getSelectedRow(), 2);
                    System.out.println("File: " + fileName);
                    String fileContents = serverFileManager.readFile(fileName);
                    ActiveData activeData = ActiveData.deserialize(fileContents);
                    activeViewerFrame.setVisible(true);
                    activeViewerFrame.resetFrame(activeData);
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panelMainPanel = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabbedPane = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableMatch = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableActive = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableTeamViewer = new javax.swing.JTable();
        labelIP = new javax.swing.JLabel();
        buttonRefrest = new javax.swing.JButton();

        setTitle("Team 467 Scouting Server");

        labelTitle.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        labelTitle.setText("Scouting Server");

        tableMatch.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableMatch.setMinimumSize(new java.awt.Dimension(300, 72));
        jScrollPane1.setViewportView(tableMatch);

        tabbedPane.addTab("Match", jScrollPane1);

        tableActive.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tableActive);

        tabbedPane.addTab("Active", jScrollPane2);

        tableTeamViewer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(tableTeamViewer);

        tabbedPane.addTab("Table Viewer", jScrollPane5);

        jScrollPane3.setViewportView(tabbedPane);

        labelIP.setText("IP:");

        buttonRefrest.setText("Refresh");
        buttonRefrest.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                buttonRefrestActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMainPanelLayout = new javax.swing.GroupLayout(panelMainPanel);
        panelMainPanel.setLayout(panelMainPanelLayout);
        panelMainPanelLayout.setHorizontalGroup(
            panelMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle)
                .addGap(18, 18, 18)
                .addComponent(labelIP, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonRefrest, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE)
        );
        panelMainPanelLayout.setVerticalGroup(
            panelMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTitle)
                    .addComponent(labelIP)
                    .addComponent(buttonRefrest))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelMainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonRefrestActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_buttonRefrestActionPerformed
    {//GEN-HEADEREND:event_buttonRefrestActionPerformed
        this.populateTables();
    }//GEN-LAST:event_buttonRefrestActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonRefrest;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel labelIP;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JPanel panelMainPanel;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JTable tableActive;
    private javax.swing.JTable tableMatch;
    private javax.swing.JTable tableTeamViewer;
    // End of variables declaration//GEN-END:variables
    @Override
    public void setVisible(boolean b)
    {
        super.setVisible(b);

        if (b)
        {
            if (this.syncFilesServerThread == null)
            {
                System.out.println("Starting server...");
                ipTryCatch:
                try
                {
                    for (final Enumeration< NetworkInterface> interfaces
                            = NetworkInterface.getNetworkInterfaces();
                            interfaces.hasMoreElements();)
                    {
                        final NetworkInterface cur = interfaces.nextElement();

                        if (cur.isLoopback())
                        {
                            continue;
                        }

                        System.out.println("interface " + cur.getName());

                        for (final InterfaceAddress addr : cur.getInterfaceAddresses())
                        {
                            final InetAddress inet_addr = addr.getAddress();

                            if (!(inet_addr instanceof Inet4Address))
                            {
                                continue;
                            }

                            labelIP.setText("IP: " + inet_addr.getHostAddress());
                            break ipTryCatch;
                        }
                    }
                    //ip not found
                    labelIP.setText("IP: Unknown");
                }
                catch (SocketException ex)
                {
                    Logger.getLogger(ServerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                syncFilesServerThread = new SyncFilesServerThread(Globals.PORT);
                syncFilesServerThread.start();
                Main.main.setMenuStartServerText(Globals.STOP_SERVER_STRING);
            }
        }
        else
        {
            if (syncFilesServerThread != null)
            {
                System.out.println("Killing server...");
                syncFilesServerThread.kill();
                syncFilesServerThread = null;
                Main.main.setMenuStartServerText(Globals.START_SERVER_STRING);
            }
        }
    }
}
