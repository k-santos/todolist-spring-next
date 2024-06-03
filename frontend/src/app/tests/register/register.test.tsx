import React from "react";
import { fireEvent, render, waitFor } from "@testing-library/react";
import RegisterForm from "@/app/register/page";

const pushMock = jest.fn();

jest.mock("next/navigation", () => ({
  useRouter: () => ({
    push: pushMock,
  }),
}));

describe("LoginForm", () => {
  beforeEach(() => {
    pushMock.mockClear();
  });

  it("should render the register form correctly", async () => {
    const { getByText, getByLabelText } = render(<RegisterForm />);
    expect(getByLabelText("Name")).toBeInTheDocument();
    expect(getByText("Register")).toBeInTheDocument();
    expect(getByLabelText("Username")).toBeInTheDocument();
    expect(getByLabelText("Confirm password")).toBeInTheDocument();
    expect(getByLabelText("Password")).toBeInTheDocument();
  });

  it("should display error message for empty fields", async () => {
    const { getByText } = render(<RegisterForm />);

    fireEvent.click(getByText("Register"));
    await waitFor(() => {
      expect(getByText("Name is required")).toBeInTheDocument();
      expect(getByText("Username is required")).toBeInTheDocument();
      expect(getByText("Password is required")).toBeInTheDocument();
      expect(getByText("Confirm password is required")).toBeInTheDocument();
    });
  });
});
