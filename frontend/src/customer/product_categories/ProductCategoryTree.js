import React, { Component } from 'react';
import { connect } from 'react-redux';
import TreeView from '@material-ui/lab/TreeView';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import TreeItem from '@material-ui/lab/TreeItem';
import {getAllProductCategories} from "./redux";

class ProductCategoryTree extends Component {

    componentDidMount() {
        this.props.getData()
    }

    render() {
        const renderTree = (node) => (
            <TreeItem key={node.id} nodeId={node.id} label={node.name}>
                {Array.isArray(node.children) ? node.children.map((node) => renderTree(node)) : null}
            </TreeItem>
        );

        return (
            <TreeView
                defaultCollapseIcon={<ExpandMoreIcon />}
                defaultExpanded={['root']}
                defaultExpandIcon={<ChevronRightIcon />}
                onNodeSelect={(event, nodeId) => this.props.onCategorySelect(nodeId)}>
                {this.props.nodes.map(renderTree)}
            </TreeView>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    return {
        nodes: state.product_categories.tree
    }
}

const mapDispatchToProps = (dispatch, ownProps) => {
    return {
        getData: rootCategoryId => {dispatch(getAllProductCategories())}
    }
}


export default connect(mapStateToProps, mapDispatchToProps)(ProductCategoryTree)