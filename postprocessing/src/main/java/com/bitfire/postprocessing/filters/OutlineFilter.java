/*******************************************************************************
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

package com.bitfire.postprocessing.filters;

import com.badlogic.gdx.math.Vector2;
import com.bitfire.utils.ShaderLoader;

public final class OutlineFilter extends Filter<OutlineFilter> {
	private Vector2 viewportInverse;

	public enum Param implements Parameter {
		Texture("u_texture0", 0), ViewportInverse("u_viewportInverse", 2), ;

		private String mnemonic;
		private int elementSize;

		private Param (String mnemonic, int arrayElementSize) {
			this.mnemonic = mnemonic;
			this.elementSize = arrayElementSize;
		}

		@Override
		public String mnemonic () {
			return this.mnemonic;
		}

		@Override
		public int arrayElementSize () {
			return this.elementSize;
		}
	}

	public OutlineFilter(int viewportWidth, int viewportHeight) {
		this(new Vector2(viewportWidth, viewportHeight), 1f / 128f, 1f / 8f, 8f);
	}

	public OutlineFilter(int viewportWidth, int viewportHeight, float fxaa_reduce_min, float fxaa_reduce_mul, float fxaa_span_max) {
		this(new Vector2(viewportWidth, viewportHeight), fxaa_reduce_min, fxaa_reduce_mul, fxaa_span_max);
	}

	public OutlineFilter(Vector2 viewportSize, float fxaa_reduce_min, float fxaa_reduce_mul, float fxaa_span_max) {
		super(ShaderLoader.fromFile("outline", "outline"));
		this.viewportInverse = viewportSize;
		this.viewportInverse.x = 1f / this.viewportInverse.x;
		this.viewportInverse.y = 1f / this.viewportInverse.y;

		rebind();
	}

	public void setViewportSize (float width, float height) {
		this.viewportInverse.set(1f / width, 1f / height);
		setParam(Param.ViewportInverse, this.viewportInverse);
	}

	public Vector2 getViewportSize () {
		return viewportInverse;
	}

	@Override
	public void rebind () {
		// reimplement super to batch every parameter
		setParams(Param.Texture, u_texture0);
		setParams(Param.ViewportInverse, viewportInverse);
		endParams();
	}

	@Override
	protected void onBeforeRender () {
		inputTexture.bind(u_texture0);
	}
}
