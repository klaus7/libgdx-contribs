/*******************************************************************************
 * Copyright 2012 tsagrista
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.bitfire.postprocessing.effects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.bitfire.postprocessing.filters.OutlineFilter;

public final class Outline extends Antialiasing {
	private OutlineFilter outlineFilter = null;

	/** Create a FXAA with the viewport size */
	public Outline(int viewportWidth, int viewportHeight) {
		setup(viewportWidth, viewportHeight);
	}

	private void setup (int viewportWidth, int viewportHeight) {
		outlineFilter = new OutlineFilter(viewportWidth, viewportHeight);
	}

	public void setViewportSize (int width, int height) {
		outlineFilter.setViewportSize(width, height);
	}

	@Override
	public void dispose () {
		if (outlineFilter != null) {
			outlineFilter.dispose();
			outlineFilter = null;
		}
	}

	@Override
	public void rebind () {
		outlineFilter.rebind();
	}

	@Override
	public void render (FrameBuffer src, FrameBuffer dest) {
		restoreViewport(dest);
		outlineFilter.setInput(src).setOutput(dest).render();
	}
}
