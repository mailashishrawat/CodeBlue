package com.indiainclusionsummit.indiainclusionsummit;

/**
 * Created by I065310 on 10/28/2015.
 */
public class Angular_Adjustment {


    public static float relative_degree;
    public static String direction_guide_me=null;
    public static String direction_status=null;
    private int DIVERT;

    public void direction_finder (float Original_degree_identified, int Direction_Identified, int take_Diversion){

//
//        Original_degree_identified:
//        0 --> NORTH
//        90 --> EAST
//        180 -->SOUTH
//        270 -->WEST

//        Direction_Identified: 0  Destination reached
//        Direction_Identified: +1 NORTH --> SOUTH
//        Direction_Identified: -1 SOUTH --> NORTH

//        take_Diversion : 0  Continue in Direction_Identified
//        take_Diversion : +1 DIVERT LEFT in NORTH--->SOUTH-->EAST         OR  DIVERT RIGHT in SOUTH--->NORTH-->EAST
//        take_Diversion : -1 DIVERT LEFT in SOUTH--->NORTH-->WEST         OR  DIVERT RIGHT in NORTH--->SOUTH-->WEST

relative_degree = Original_degree_identified;

        // for iis we have 82 degree deviation.
        if (Original_degree_identified > 82)
            relative_degree = relative_degree - 82;
        else if (Original_degree_identified >= 0 && Original_degree_identified <= 82)
            relative_degree = relative_degree - 82 + 360;

DIVERT = take_Diversion;

        if(Direction_Identified == 0)
        {
            direction_guide_me = "DESTINATION ARRIVED";
        }

        //        Direction_Identified: +1 SOUTH --> NORTH
//GO UPWARDS from SOUTH TO NORTH WITHOUT DIVERSIONS
        if(Direction_Identified == -1 )

        {
            if( DIVERT == 0 || DIVERT == 1)
            {
                direction_status = "SOUTH TO NORTH MOVE";
                if (relative_degree >= 0 && relative_degree < 40) {

                    direction_guide_me = "GO STRAIGHT UPWARDS";
                }
                if (relative_degree >= 40 && relative_degree < 80) {

                    direction_guide_me = "TURN SLIGHT LEFT";
                }
                if (relative_degree >= 80 && relative_degree < 140) {

                    direction_guide_me = "TURN LEFT";
                }
                if (relative_degree >= 140 && relative_degree < 230) {

                    direction_guide_me = "TURN BACK";
                }
                if (relative_degree >= 230 && relative_degree < 290) {

                    direction_guide_me = "TURN RIGHT";
                }
                if (relative_degree >= 290 && relative_degree < 330) {

                    direction_guide_me = "TURN SLIGHT RIGHT";
                }
                if (relative_degree >= 330 && relative_degree < 360) {

                    direction_guide_me = "GO STRAIGHT UPWARDS";
                }
            }
        }


//        Direction_Identified: -1 SOUTH --> NORTH
//GO UPWARDS from SOUTH TO NORTH WITH DIVERSIONS
//        take_Diversion : -1 DIVERT LEFT in SOUTH--->NORTH-->WEST
//TURN LEFT DIRECTIONS UPWARDS

        if(Direction_Identified == -1 )
        {
            if(DIVERT == -1)
            {
                            direction_status = "SOUTH TO NORTH MOVE WITH DIVERSION LEFT";

                            if (relative_degree >= 0 && relative_degree < 50) {

                                direction_guide_me = "TURN LEFT";
                            }
                            if (relative_degree >= 50 && relative_degree < 140) {

                                direction_guide_me = "TURN BACK";
                            }
                            if (relative_degree >= 140 && relative_degree < 200) {

                                direction_guide_me = "TURN RIGHT";
                            }
                            if (relative_degree >= 200 && relative_degree < 240) {

                                direction_guide_me = "TURN SLIGHT RIGHT";
                            }
                            if (relative_degree >= 240 && relative_degree < 310) {

                                direction_guide_me = "GO STRAIGHT";
                            }
                            if (relative_degree >= 310 && relative_degree < 350) {

                                direction_guide_me = "TURN SLIGHT LEFT";
                            }
                            if (relative_degree >= 350 && relative_degree < 360) {

                                direction_guide_me = "TURN LEFT";
                            }
            }
        }



//        Direction_Identified: +1 NORTH --> SOUTH
//GO DOWNWARDS from NORTH TO SOUTH WITHOUT DIVERSIONS
        if(Direction_Identified == 1 )
        {
            if(DIVERT == 0 || DIVERT == -1) {
                direction_status = "NORTH TO SOUTH MOVE";
                if (relative_degree >= 0 && relative_degree < 50) {

                    direction_guide_me = "TURN BACK";
                }
                if (relative_degree >= 50 && relative_degree < 110) {

                    direction_guide_me = "TURN RIGHT";
                }
                if (relative_degree >= 110 && relative_degree < 150) {

                    direction_guide_me = "TURN SLIGHT RIGHT";
                }
                if (relative_degree >= 150 && relative_degree < 220) {

                    direction_guide_me = "GO STRAIGHT DOWNWARDS";
                }
                if (relative_degree >= 220 && relative_degree < 260) {

                    direction_guide_me = "TURN SLIGHT LEFT";
                }
                if (relative_degree >= 260 && relative_degree < 320) {

                    direction_guide_me = "TURN  LEFT";
                }
                if (relative_degree >= 320 && relative_degree < 360) {

                    direction_guide_me = "TURN BACK";
                }
            }
        }


//        Direction_Identified: +1 NORTH --> SOUTH
//       GO DOWNWARDS from NORTH TO SOUTH WITH DIVERSIONS
//        take_Diversion : +1 DIVERT LEFT in NORTH--->SOUTH-->EAST

        if(Direction_Identified == 1 )
        {
            if(DIVERT == 1) {
                direction_status = "NORTH TO SOUTH MOVE WITH DIVERSION LEFT";
                if (relative_degree >= 0 && relative_degree < 20) {

                    direction_guide_me = "TURN RIGHT";
                }
                if (relative_degree >= 20 && relative_degree < 60) {

                    direction_guide_me = "TURN SLIGHT RIGHT";
                }
                if (relative_degree >= 60 && relative_degree < 130) {

                    direction_guide_me = "GO STRAIGHT ";
                }
                if (relative_degree >= 130 && relative_degree < 170) {

                    direction_guide_me = "TURN SLIGHT LEFT";
                }
                if (relative_degree >= 170 && relative_degree < 230) {

                    direction_guide_me = "TURN LEFT";
                }
                if (relative_degree >= 230 && relative_degree < 320) {

                    direction_guide_me = "TURN BACK";
                }
                if (relative_degree >= 320 && relative_degree < 360) {

                    direction_guide_me = "TURN RIGHT";
                }
            }
        }


    }

}
