import React from 'react';
import { useParams } from "react-router-dom";

export default function Page(props) {
    let params = useParams();

    const childrenWithProps = React.Children.map(
        props.children,
            child => React.isValidElement(child) ? React.cloneElement(child, params): child);

    return childrenWithProps;
}