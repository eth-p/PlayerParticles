/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Slikey
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStyleCompanion extends DefaultParticleStyle {

    private int numParticles = 150;
    private int particlesPerIteration = 5;
    private double size = 1.25;
    private double xFactor = 1.0, yFactor = 1.5, zFactor = 1.0;
    private double xOffset = 0.0, yOffset = -0.75, zOffset = 0.0;
    private int step = 0;

    public ParticleStyleCompanion() {
        super("companion", true, false, 1);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        Vector vector = new Vector();
        
        double t = (Math.PI / this.numParticles) * this.step;
        double r = Math.sin(t) * this.size;
        double s = 2 * Math.PI * t;

        vector.setX(this.xFactor * r * Math.cos(s) + this.xOffset);
        vector.setZ(this.zFactor * r * Math.sin(s) + this.zOffset);
        vector.setY(this.yFactor * this.size * Math.cos(t) + this.yOffset);

        for (int i = 0; i < this.particlesPerIteration; i++) {
            particles.add(new PParticle(location.clone().subtract(vector)));
        }

        return particles;
    }

    @Override
    public void updateTimers() {
        this.step++;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
