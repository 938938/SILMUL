import styled from 'styled-components';

export const StyledToggle = styled.div`
  display: inline-block;
  position: relative;
  width: 60px;
  height: 34px;
  border-radius: 34px;
  cursor: pointer;
  background-color: ${(props) => props.theme.themeStyle.toggleColor};
`;

export const ToggleSwitch = styled.div`
  position: absolute;
  top: 2px;
  left: ${(props) => props.theme.themeStyle.togglePosition};
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #fff;
  box-shadow: 0 0 2px rgba(0, 0, 0, 0.3);
  transition: left 0.3s;
`;
