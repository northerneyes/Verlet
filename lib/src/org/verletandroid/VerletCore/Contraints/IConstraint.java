package org.verletandroid.VerletCore.Contraints;

import org.verletandroid.VerletCore.IEntity;

/**
 * Created with IntelliJ IDEA.
 * User: George
 * Date: 28.04.13
 * Time: 23:19
 * To change this template use File | Settings | File Templates.
 */
public interface IConstraint extends IEntity {
    void relax(float stepCoef);
}
