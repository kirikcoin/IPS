package mobi.eyeline.ips.web.controllers

import mobi.eyeline.ips.components.tree.TreeNode

@SuppressWarnings(['GrMethodMayBeStatic', 'GrUnresolvedAccess'])
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
        link 1, [2, 3, 0]
        link 2, [4]*3
        link 3, [5]*3
        link 4, ['end']*3
        link 5, ['end']*3
        root 1
    }

    def biggerNonlinear1 = tree([
            end: new TreeNode(end, 'Конец опроса', null)
    ]) {
        link 1, [2, 3, 4]
        link 2, [7, 4]
        link 3, [5, 6]
        link 4, [6, 'end']
        link 5, [6, 'end']
        link 6, ['end', 'end']
        link 7, [5, 6]
        root 1
    }

    def smallLinear = tree([
            end: new TreeNode(end, 'Конец опроса', null)
    ]) {
        link 1, [2]*3
        link 2, [3]*2
        link 3, [4]*5
        link 4, ['end']*3
        root 1
    }

    def longLinear = tree([
            end: new TreeNode(end, 'Конец опроса', null)
    ]) {
        link 1, [2]*3
        (2..15).each { n -> link n, [n+1]*2 }
        link 16, ['end']*3
        root 1
    }

    def wideLinear = tree([
            end: new TreeNode(end, 'Конец опроса', null)
    ]) {
        link 1, [2]*7
        link 2, [3]*10
        link 3, [4]*5
        link 4, [5]*12
        link 5, ['end']*3
        root 1
    }
}

