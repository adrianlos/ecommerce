import React from 'react';
import { Breadcrumbs, Typography } from '@material-ui/core';

export default function ProductCategoryTree(props) {
    return (
        <Breadcrumbs aria-label="breadcrumb">
            <Typography color="textPrimary">Cały asortyment</Typography>
            {props.nodes.map(node => <Typography key={node.id} color="textPrimary">{node.name}</Typography>)}
        </Breadcrumbs>
    );
}
