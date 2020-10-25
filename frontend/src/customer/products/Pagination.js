import React from "react";
import {IconButton} from "@material-ui/core";
import ArrowLeftIcon from '@material-ui/icons/ArrowLeft';
import ArrowRightIcon from '@material-ui/icons/ArrowRight';

export default function Pagination(props) {
    return (
        <div>
            <IconButton onClick={ props.goToPreviousPage }><ArrowLeftIcon /></IconButton>
            <span>{props.no}/{props.count}</span>
            <IconButton onClick={ props.goToNextPage }><ArrowRightIcon /></IconButton>
        </div>
    );
}