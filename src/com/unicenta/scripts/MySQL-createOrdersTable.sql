/*  uniCenta oPOS  - Touch Friendly Point Of Sale
 *  Copyright (c) 2017 uniCenta
 *  https://unicenta.com
 *
 *  This file is part of uniCenta Remote Display
 *
 *  uniCenta Remote Display is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  uniCenta Remote Display is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with uniCenta oPOS.  If not, see <http://www.gnu.org/licenses/>.
 *
*/

/**
 * Author:  uniCenta
 * Created: 01-Jan-2017
 * Creating for uniCenta Remote Display
*/


/* Header line. Object: orders. Script date: 01/01/2017 00:00:01. */
CREATE TABLE IF NOT EXISTS `orders` (
    `id` int(11) NOT NULL,
    `orderid` varchar(50) DEFAULT NULL,
    `qty` int(11) DEFAULT '1',
    `details` varchar(255) DEFAULT NULL,
    `attributes` varchar(255) DEFAULT NULL,
    `notes` varchar(255) DEFAULT NULL,
    `ticketid` varchar(50) DEFAULT NULL,
    `ordertime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `displayid` int(11) DEFAULT '1',
    `auxiliary` int(11) DEFAULT NULL,
    `completetime` timestamp DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;