package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.components.tree.TreeNode

@SuppressWarnings(['GrMethodMayBeStatic', 'GroovyAssignabilityCheck', 'GrUnresolvedAccess'])
@Mixin(TreeTestHelpers)
class TreeTestController {

    def width = 1168
    def height = 400

    def end = 0

    //
    //  Note: this is a test code, made to be as compact as possible. 
    //  Please, do not bring things like this (and TreeTestHelpers) to the main codebase!
    //

    def smallNonlinear = tree([
            end: new TreeNode(end, 'Конец опроса', null)
    ]) {
        link it, 1, [2, 3, 0]
        link it, 2, [4]*3
        link it, 3, [5]*3
        link it, 4, ['end']*3
        link it, 5, ['end']*3
        root it, 1
    }

    def biggerNonlinear1 = tree([
            end: new TreeNode(end, 'Конец опроса', null)
    ]) {
        link it, 1, [2, 3, 4]
        link it, 2, [7, 4]
        link it, 3, [5, 6]
        link it, 4, [6, 'end']
        link it, 5, [6, 'end']
        link it, 6, ['end', 'end']
        link it, 7, [5, 6]
        root it, 1
    }

    def smallLinear = tree([
            end: new TreeNode(end, 'Конец опроса', null)
    ]) {
        link it, 1, [2]*3
        link it, 2, [3]*2
        link it, 3, [4]*5
        link it, 4, ['end']*3
        root it, 1
    }

    def longLinear = tree([
            end: new TreeNode(end, 'Конец опроса', null)
    ]) {
        link it, 1, [2]*3
        link it, 2, [3]*2
        link it, 3, [4]*2
        link it, 4, [5]*2
        link it, 5, [6]*2
        link it, 6, [7]*2
        link it, 7, [8]*2
        link it, 8, [9]*2
        link it, 9, [10]*2
        link it, 10, [11]*2
        link it, 11, [12]*2
        link it, 12, [13]*2
        link it, 13, [14]*2
        link it, 14, [15]*2
        link it, 15, [16]*2
        link it, 16, ['end']*3
        root it, 1
    }

    def wideLinear = tree([
            end: new TreeNode(end, 'Конец опроса', null)
    ]) {
        link it, 1, [2]*7
        link it, 2, [3]*10
        link it, 3, [4]*5
        link it, 4, [5]*12
        link it, 5, ['end']*3
        root it, 1
    }
}

