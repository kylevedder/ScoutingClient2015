/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.objects.activedata;

import client.objects.UserDataInterface;
import client.objects.ObjectType;
import client.objects.matchdata.CoOpType;
import client.objects.matchdata.HumanPlayerType;
import client.objects.matchdata.MatchData;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author kyle
 */
public class ActiveData implements UserDataInterface
{

    int matchTeamNumber = -1;
    public static String matchTeamNumberKey = "matchTeamNumberKey";
    String matchRobotScouter = null;
    public static String matchRobotScouterKey = "matchRobotScouterKey";

    RobotShape robotShape = null;
    public static String robotShapeKey = "robotShapeKey";
    RobotNumWheels robotNumWheels = null;
    public static String robotNumWheelsKey = "robotNumWheelsKey";
    RobotWheelType robotWheelType = null;
    public static String robotWheelTypeKey = "robotWheelTypeKey";
    String robotComments = null;
    public static String robotCommentsKey = "robotCommentsKey";

    int autoNumTotes = -1;
    public static String autoNumTotesKey = "autoNumTotes";
    int autoNumContainers = 1;
    public static String autoNumContainersKey = "autoNumContainersKey";
    boolean autoTotesStacked = false;
    public static String autoTotesStackedKey = "autoTotesStackedKey";
    boolean autoInAutoZone = false;
    public static String autoInAutoZoneKey = "autoInAutoZoneKey";

    boolean toteCanGetTotes = false;
    public static String toteCanGetTotesKey = "toteCanGetTotesKey";
    TotePickupOrientation totePickupOrientation = null;
    public static String totePickupOrientationKey = "totePickupOrientationKey";
    int toteMaxStackHeight = -1;
    public static String toteMaxStackHeightKey = "toteMaxStackHeightKey";
    FeedLocation toteFeedLocation = null;
    public static String toteFeedLocationKey = "toteFeedLocationKey";

    boolean containerCanGetContainers = false;
    public static String containerCanGetContainersKey = "containerCanGetContainersKey";
    boolean containerMustBeUpright = false;
    public static String containerMustBeUprightKey = "containerMustBeUprightKey";
    int containerMaxCappableStackHeight = -1;
    public static String containerMaxCappableStackHeightKey = "containerMaxCappableStackHeightKey";

    boolean litterCanPushLitter = false;
    public static String litterCanPushLitterKey = "litterCanPushLitterKey";
    boolean litterCanPickupLitter = false;
    public static String litterCanPickupLitterKey = "litterCanPickupLitterKey";

    CoOpType coopType = null;
    public static String coopTypeKey = "coopTypeKey";
    HumanPlayerType humanPlayerType = null;
    public static String humanPlayerTypeKey = "humanPlayerTypeKey";

    /**
     * Holds all data about a paticular team from active scouting.
     *
     * @param matchTeamNumber
     * @param matchScouter
     * @param robotShape
     * @param robotNumWheels
     * @param robotWheelType
     * @param robotComments
     * @param autoNumTotes
     * @param autoNumContainers
     * @param autoTotesStacked
     * @param autoInAutoZone
     * @param toteCanGetTotes
     * @param totePickupOrientation
     * @param toteMaxStackHeight
     * @param toteFeedLocation
     * @param containerCanGetContainers
     * @param containerMustBeUpright
     * @param containerMaxCappableStackHeight
     * @param litterCanPushLitter
     * @param litterCanPickupLitter
     * @param coopType
     * @param humanPlayerType
     */
    public ActiveData(int matchTeamNumber, String matchScouter, //scouter info
            RobotShape robotShape, RobotNumWheels robotNumWheels, RobotWheelType robotWheelType, String robotComments,//robot type
            int autoNumTotes, int autoNumContainers, boolean autoTotesStacked, boolean autoInAutoZone,//auto info
            boolean toteCanGetTotes, TotePickupOrientation totePickupOrientation, int toteMaxStackHeight, FeedLocation toteFeedLocation,//tote info 
            boolean containerCanGetContainers, boolean containerMustBeUpright, int containerMaxCappableStackHeight, //container info
            boolean litterCanPushLitter, boolean litterCanPickupLitter, //litter info
            CoOpType coopType, //coop info
            HumanPlayerType humanPlayerType //human player info                      
    )
    {
        this.matchTeamNumber = matchTeamNumber;
        this.matchRobotScouter = matchScouter;

        this.robotShape = robotShape;
        this.robotNumWheels = robotNumWheels;
        this.robotWheelType = robotWheelType;
        this.robotComments = robotComments;

        this.autoInAutoZone = autoInAutoZone;
        this.autoNumContainers = autoNumContainers;
        this.autoTotesStacked = autoTotesStacked;
        this.autoNumTotes = autoNumTotes;

        this.toteCanGetTotes = toteCanGetTotes;
        this.toteFeedLocation = toteFeedLocation;
        this.toteMaxStackHeight = toteMaxStackHeight;
        this.totePickupOrientation = totePickupOrientation;

        this.containerCanGetContainers = containerCanGetContainers;
        this.containerMaxCappableStackHeight = containerMaxCappableStackHeight;
        this.containerMustBeUpright = containerMustBeUpright;

        this.coopType = coopType;

        this.humanPlayerType = humanPlayerType;
    }

    /**
     * Converts the active data object to JSON String
     *
     * @return
     */
    public String serialize()
    {
        Map<String, Object> map = new HashMap<>();
        map.put(matchTeamNumberKey, matchTeamNumber);
        map.put(matchRobotScouterKey, matchRobotScouter);

        map.put(autoInAutoZoneKey, autoInAutoZone);
        map.put(autoNumContainersKey, autoNumContainers);
        map.put(autoNumTotesKey, autoNumTotes);
        map.put(autoTotesStackedKey, autoTotesStacked);

        map.put(toteCanGetTotesKey, toteCanGetTotes);
        map.put(toteFeedLocationKey, toteFeedLocation.toString());
        map.put(toteMaxStackHeightKey, toteMaxStackHeight);
        map.put(totePickupOrientationKey, totePickupOrientation.toString());

        map.put(containerCanGetContainersKey, containerCanGetContainers);
        map.put(containerMaxCappableStackHeightKey, containerMaxCappableStackHeight);
        map.put(containerMustBeUprightKey, containerMustBeUpright);

        map.put(robotShapeKey, robotShape.toString());
        map.put(robotWheelTypeKey, robotWheelType.toString());
        map.put(robotNumWheelsKey, robotNumWheels.toString());
        map.put(robotCommentsKey, robotComments);

        map.put(litterCanPickupLitterKey, litterCanPickupLitter);
        map.put(litterCanPushLitterKey, litterCanPushLitter);

        map.put(coopTypeKey, coopType.toString());

        map.put(humanPlayerTypeKey, humanPlayerType.toString());

        map.put(this.KEY_OBJECT_TYPE, this.getType().toString());
        JSONObject json = new JSONObject(map);
        return json.toString();
    }

    /**
     * Converts a JSON string into an ActiveData object.
     *
     * @param jsonStr
     * @return
     */
    public static ActiveData deserialize(String jsonStr)
    {
        JSONObject json = new JSONObject(jsonStr);
        int matchTeamNumber = json.getInt(matchTeamNumberKey);
        String matchScouter = json.getString(matchRobotScouterKey);
        String robotComments = json.getString(robotCommentsKey);
        RobotShape robotShape = RobotShape.valueOf(json.getString(robotShapeKey));
        RobotNumWheels robotNumWheels = RobotNumWheels.valueOf(json.getString(robotNumWheelsKey));
        RobotWheelType robotWheelType = RobotWheelType.valueOf(json.getString(robotWheelTypeKey));
        int autoNumTotes = json.getInt(autoNumTotesKey);
        int autoNumContainers = json.getInt(autoNumContainersKey);
        boolean autoTotesStacked = json.getBoolean(autoTotesStackedKey);
        boolean autoInAutoZone = json.getBoolean(autoInAutoZoneKey);
        boolean toteCanGetTotes = json.getBoolean(toteCanGetTotesKey);
        TotePickupOrientation totePickupOrientation = TotePickupOrientation.valueOf(json.getString(totePickupOrientationKey));
        int toteMaxStackHeight = json.getInt(toteMaxStackHeightKey);
        FeedLocation toteFeedLocation = FeedLocation.valueOf(json.getString(toteFeedLocationKey));
        boolean containerCanGetContainers = json.getBoolean(containerCanGetContainersKey);
        boolean containerMustBeUpright = json.getBoolean(containerMustBeUprightKey);
        int containerMaxCappableStackHeight = json.getInt(containerMaxCappableStackHeightKey);
        boolean litterCanPushLitter = json.getBoolean(litterCanPushLitterKey);
        boolean litterCanPickupLitter = json.getBoolean(litterCanPickupLitterKey);
        CoOpType coopType = CoOpType.valueOf(json.getString(coopTypeKey));
        HumanPlayerType humanPlayerType = HumanPlayerType.valueOf(json.getString(humanPlayerTypeKey));
        return new ActiveData(matchTeamNumber, matchScouter, robotShape, robotNumWheels, robotWheelType, robotComments, autoNumTotes, autoNumContainers, autoTotesStacked, autoInAutoZone, toteCanGetTotes, totePickupOrientation, toteMaxStackHeight, toteFeedLocation, containerCanGetContainers, containerMustBeUpright, containerMaxCappableStackHeight, litterCanPushLitter, litterCanPickupLitter, coopType, humanPlayerType);
    }

    public int getAutoNumContainers()
    {
        return autoNumContainers;
    }

    public int getAutoNumTotes()
    {
        return autoNumTotes;
    }

    public int getContainerMaxCappableStackHeight()
    {
        return containerMaxCappableStackHeight;
    }

    public CoOpType getCoopType()
    {
        return coopType;
    }

    public HumanPlayerType getHumanPlayerType()
    {
        return humanPlayerType;
    }

    public String getMatchRobotScouter()
    {
        return matchRobotScouter;
    }

    public int getMatchTeamNumber()
    {
        return matchTeamNumber;
    }

    public String getRobotComments()
    {
        return robotComments;
    }

    public RobotNumWheels getRobotNumWheels()
    {
        return robotNumWheels;
    }

    public RobotShape getRobotShape()
    {
        return robotShape;
    }

    public RobotWheelType getRobotWheelType()
    {
        return robotWheelType;
    }

    public FeedLocation getToteFeedLocation()
    {
        return toteFeedLocation;
    }

    public int getToteMaxStackHeight()
    {
        return toteMaxStackHeight;
    }

    public TotePickupOrientation getTotePickupOrientation()
    {
        return totePickupOrientation;
    }

    public boolean getAutoInAutoZone()
    {
        return autoInAutoZone;
    }

    public boolean getAutoTotesStacked()
    {
        return autoTotesStacked;
    }

    public boolean getContainerCanGetContainers()
    {
        return containerCanGetContainers;
    }

    public boolean getContainerMustBeUpright()
    {
        return containerMustBeUpright;
    }

    public boolean getLitterCanPickupLitter()
    {
        return litterCanPickupLitter;
    }

    public boolean getLitterCanPushLitter()
    {
        return litterCanPushLitter;
    }

    public boolean getToteCanGetTotes()
    {
        return toteCanGetTotes;
    }

    public ObjectType getType()
    {
        return ObjectType.ACTIVE;
    }

    public String getFileName()
    {
        return String.valueOf(this.getType().toString()) + "_"
                + String.valueOf(String.valueOf(this.getMatchTeamNumber() + "_"
                                + String.valueOf(this.getMatchRobotScouter().trim().replaceAll(" ", "_").replace("\\", "").replace("/", "").replace(".", ""))
                                + "_" + String.valueOf(this.serialize().hashCode()) + ".json"));
    }

    @Override
    public int getNumParams()
    {
        return getAllParams().length;
    }

    @Override
    public Object[] getAllParams()
    {
        Object[] objs = new Object[]
        {
            matchTeamNumber,
            matchRobotScouter,
            robotShape,
            robotNumWheels,
            robotWheelType,
            robotComments,
            autoInAutoZone,
            autoNumContainers,
            autoTotesStacked,
            autoNumTotes,
            toteCanGetTotes,
            toteFeedLocation,
            toteMaxStackHeight,
            totePickupOrientation,
            containerCanGetContainers,
            containerMaxCappableStackHeight,
            containerMustBeUpright,
            coopType,
            humanPlayerType
        };
        return objs;
    }

}
