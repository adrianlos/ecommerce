import React from 'react';
import TreeView from '@material-ui/lab/TreeView';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import TreeItem from '@material-ui/lab/TreeItem';


export default function ProductCategoryTree(props) {
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
            onNodeSelect={(event, nodeId) => props.onCategorySelect(nodeId)}>
            {props.nodes.map(renderTree)}
        </TreeView>
    );
}