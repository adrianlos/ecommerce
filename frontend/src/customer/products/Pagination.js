import React from "react";
import {IconButton} from "@material-ui/core";
import ArrowLeftIcon from '@material-ui/icons/ArrowLeft';
import ArrowRightIcon from '@material-ui/icons/ArrowRight';

export default function Pagination(props) {
    return (
        <div>
            <IconButton disabled={props.no <= 0}
                        onClick={ props.goToPreviousPage }
                        style={{ paddingTop: "0px", paddingBottom: "0px" }}>
                <ArrowLeftIcon />
            </IconButton>
            <span style={{fontSize: "16px"}}>{props.no + 1}/{props.count || "..."}</span>
            <IconButton disabled={props.no + 1 >= props.count}
                        onClick={ props.goToNextPage }
                        style={{ paddingTop: "0px", paddingBottom: "0px" }}>
                <ArrowRightIcon />
            </IconButton>
        </div>
    );
}