/* ========================================================================
 * PlantUML : a free UML diagram generator
 * ========================================================================
 *
 * (C) Copyright 2009-2014, Arnaud Roques
 *
 * Project Info:  http://plantuml.sourceforge.net
 * 
 * This file is part of PlantUML.
 *
 * PlantUML is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PlantUML distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * Original Author:  Arnaud Roques
 * 
 * Revision $Revision: 4636 $
 *
 */
package net.sourceforge.plantuml.sequencediagram.teoz;

import java.awt.geom.Dimension2D;

import net.sourceforge.plantuml.graphic.StringBounder;
import net.sourceforge.plantuml.real.Real;
import net.sourceforge.plantuml.real.RealUtils;
import net.sourceforge.plantuml.sequencediagram.Delay;
import net.sourceforge.plantuml.sequencediagram.Event;
import net.sourceforge.plantuml.skin.Area;
import net.sourceforge.plantuml.skin.Component;
import net.sourceforge.plantuml.skin.ComponentType;
import net.sourceforge.plantuml.skin.Context2D;
import net.sourceforge.plantuml.ugraphic.UGraphic;
import net.sourceforge.plantuml.ugraphic.UTranslate;

public class DelayTile implements Tile, TileWithCallbackY {

	private final Delay delay;
	private final TileArguments tileArguments;
	// private Real first;
	// private Real last;
	private Real middle;
	private double y;

	public Event getEvent() {
		return delay;
	}

	public void callbackY(double y) {
		this.y = y;
	}

	public DelayTile(Delay delay, TileArguments tileArguments) {
		this.delay = delay;
		this.tileArguments = tileArguments;
	}

	private void init(StringBounder stringBounder) {
		if (middle != null) {
			return;
		}
		final Real first = tileArguments.getFirstLivingSpace().getPosC(stringBounder);
		final Component comp = getComponent(stringBounder);
		final Real last = tileArguments.getLastLivingSpace().getPosC(stringBounder);
		this.middle = RealUtils.middle(first, last);

	}

	private Component getComponent(StringBounder stringBounder) {
		final Component comp = tileArguments.getSkin().createComponent(ComponentType.DELAY_TEXT, null,
				tileArguments.getSkinParam(), delay.getText());
		return comp;
	}

	private double getPreferredWidth(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim.getWidth();
	}

	public void drawU(UGraphic ug) {
		final StringBounder stringBounder = ug.getStringBounder();
		init(stringBounder);
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		final Area area = new Area(getPreferredWidth(stringBounder), dim.getHeight());
		tileArguments.getLivingSpaces().delayOn(y, dim.getHeight());

		ug = ug.apply(new UTranslate(getMinX(stringBounder).getCurrentValue(), 0));
		comp.drawU(ug, area, (Context2D) ug);
	}

	public double getPreferredHeight(StringBounder stringBounder) {
		final Component comp = getComponent(stringBounder);
		final Dimension2D dim = comp.getPreferredDimension(stringBounder);
		return dim.getHeight();
	}

	public void addConstraints(StringBounder stringBounder) {
	}

	public Real getMinX(StringBounder stringBounder) {
		init(stringBounder);
		return this.middle.addFixed(-getPreferredWidth(stringBounder) / 2);
	}

	public Real getMaxX(StringBounder stringBounder) {
		init(stringBounder);
		return this.middle.addFixed(getPreferredWidth(stringBounder) / 2);
	}

	// private double startingY;
	//
	// public void setStartingY(double startingY) {
	// this.startingY = startingY;
	// }
	//
	// public double getStartingY() {
	// return startingY;
	// }

}