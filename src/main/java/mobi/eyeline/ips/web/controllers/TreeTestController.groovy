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

    def smallNonlinear = [
            end: new TreeNode(end, 'Конец опроса', null)
    ].withDefault(this.&q).with { _ ->
        link _[1], [_[2], _[3], _['end']]
        link _[2], [_[4]]*3
        link _[3], [_[5]]*3
        link _[4], [_['end']]*3
        link _[5], [_['end']]*3
        _[1]
    }

    def smallLinear = [
            end: new TreeNode(end, 'Конец опроса', null)
    ].withDefault(this.&q).with { _ ->
        link _[1], [_[2]]*3
        link _[2], [_[3]]*2
        link _[3], [_[4]]*5
        link _[4], [_['end'], _['end'], _['end']]
        _[1]
    }

    def longLinear = [
            end: new TreeNode(end, 'Конец опроса', null)
    ].withDefault(this.&q).with { _ ->
        link _[1], [_[2]]*3
        link _[2], [_[3]]*2
        link _[3], [_[4]]*2
        link _[4], [_[5]]*2
        link _[5], [_[6]]*2
        link _[6], [_[7]]*2
        link _[7], [_[8]]*2
        link _[8], [_[9]]*2
        link _[9], [_[10]]*2
        link _[10], [_[11]]*2
        link _[11], [_[12]]*2
        link _[12], [_[13]]*2
        link _[13], [_[14]]*2
        link _[14], [_[15]]*2
        link _[15], [_[16]]*2
        link _[16], [_['end']]*3
        _[1]
    }

    def wideLinear = [
            end: new TreeNode(end, 'Конец опроса', null)
    ].withDefault(this.&q).with { _ ->
        link _[1], [_[2]]*7
        link _[2], [_[3]]*10
        link _[3], [_[4]]*5
        link _[4], [_[5]]*12
        link _[5], [_['end']]*3
        _[1]
    }
}

