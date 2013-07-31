/**
 * @(#)Fudge.java
 *
 *
 * @by Carl Celizic
 * @version 1.00 2010/11/17
 */

import java.lang.Double;
import java.util.Random;
import java.lang.Integer;


public class HERO
{
	//now to throw up the infamous HERO system speed chart
	public static boolean[][] SPD = {
	//  1	   2	  3      4      5      6      7      8      9      10     11     12
      { false, false, false, false, false, false, true,  false, false, false, false, false },
      { false, false, false, false, false, true,  false, false, false, false, false, true  },
      { false, false, false, true,  false, false, false, true,  false, false, false, true  },
      { false, false, true,  false, false, true,  false, false, true,  false, false, true  },
      { false, false, true,  false, true,  false, false, true,  false, true,  false, true  },
      { false, true,  false, true,  false, true,  false, true,  false, true,  false, true  },
      { false, true,  false, true,  false, true,  true,  false, true,  false, true,  true  },
      { false, true,  true,  false, true,  true,  false, true,  true,  false, true,  true  },
      { false, true,  true,  true,  false, true,  true,  true,  false, true,  true,  true  },
      { false, true,  true,  true,  true,  true,  false, true,  true,  true,  true,  true  },
      { false, true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true  },
      { true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true,  true  }
    };

	//a little function to see if someone has init on that segment
	public static boolean actsOnSegment(int speed, int segment)
	{
		//set segment and speed within 1-12
		if (speed > 12 )
			speed = 12;
		else if ( speed < 1 )
			speed = 1;
		if (segment > 12 )
			segment = 12;
		else if ( segment < 1 )
			segment = 1;
		//and then return the array position
		return SPD[speed-1][segment-1];
	}
}
