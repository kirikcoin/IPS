package mobi.eyeline.ips.utils

import mobi.eyeline.ips.components.tree.TreeNode
import mobi.eyeline.ips.util.GraphUtil

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.hasSize
import static org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace

@Mixin(TreeBuilder)
class GraphUtilTest extends GroovyTestCase {

    void test1() {

        def tree = tree([
                end: new TreeNode(0, 'Конец опроса', null),
                u1: new TreeNode(10, 'u1', null),
                u2: new TreeNode(20, 'u2', null)
        ]) {
            link 1, [2, 3, 0]
            link 2, [4]*3
            link 3, [5]*3
            link 'u1', [5]*3
            link 'u2', [1, 2]
            link 4, ['end']*3
            link 5, ['end']*3
            root 1
        }

        assertThat GraphUtil.findCycles(tree), hasSize(0)
        assertEquals(
                GraphUtil.findUnreachable(tree, [tree, q(10), q(20)]).collect {it.label}, ['10', '20'])
    }

    void test1Text() {
        def tree = tree([
                end: new TreeNode(0, 'Конец опроса', null),
                u1: new TreeNode(10, 'u1', null),
                u2: new TreeNode(20, 'u2', null)
        ]) {
            link 1, [2, 3, 0]
            link 2, [4]*3
            link 3, [5]*3
            link 'u1', [5]*3
            link 'u2', [1, 2]
            link 4, ['end']*3
            link 5, ['end']*3
            root 1
        }

        assertThat tree.describe(), equalToIgnoringWhiteSpace('''
            Root: [1]

            [1] --1--> [2]
            [1] --2--> [3]
            [1] --3--> [0]

            [2] --1--> [4]
            [2] --2--> [4]
            [2] --3--> [4]

            [4] --1--> [0]
            [4] --2--> [0]
            [4] --3--> [0]


            [3] --1--> [5]
            [3] --2--> [5]
            [3] --3--> [5]

            [5] --1--> [0]
            [5] --2--> [0]
            [5] --3--> [0]
            ''')
    }

    void test2() {

        def tree = tree([
                end: new TreeNode(0, 'Конец опроса', null),
        ]) {
            link 1, [2, 3, 0]
            link 2, [0]
            link 0, 2
            root 2
        }

        assertEquals([[q(2), q(0)]], GraphUtil.findCycles(tree))
    }

    void test3() {

        def tree = tree([
                end: new TreeNode(0, 'Конец опроса', null),
        ]) {
            link 1, [2, 3, 0]
            link 2, [0]
            link 0, [1, 3]
            link 3, [1]
            root 2
        }

        assertEquals([
                [q(2), q(0), q(1)],
                [q(2), q(0), q(3), q(1)],
                [q(0), q(1)],
                [q(0), q(3), q(1)],
                [q(1), q(3)]],
                GraphUtil.findCycles(tree))
    }
}
