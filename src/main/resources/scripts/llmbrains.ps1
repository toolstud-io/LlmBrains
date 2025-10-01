param(
    [Parameter(Mandatory=$true)]
    [string]$Subcommand,

    [Parameter(ValueFromRemainingArguments=$true)]
    [string[]]$Arguments
)

$ErrorActionPreference = "Stop"

function Show-Usage {
    Write-Host @"
Usage:
  llmbrains.ps1 check <friendly-name> <version-command> [install-hint]
  llmbrains.ps1 update <friendly-name> <binary> <update-command> [install-hint]
  llmbrains.ps1 check-all <agent-definitions>
  llmbrains.ps1 update-all <agent-definitions> <active-agent-ids>
"@
}

function Invoke-Check {
    param($Name, $VersionCommand, $InstallHint)

    $binary = $VersionCommand -split ' ' | Select-Object -First 1
    if ([string]::IsNullOrEmpty($binary)) {
        Write-Error "Could not determine binary for $Name"
        return
    }

    if (Get-Command $binary -ErrorAction SilentlyContinue) {
        try {
            $versionOutput = Invoke-Expression "$VersionCommand 2>&1" | Out-String
            Write-Host "- $Name is installed: $($versionOutput.Trim())"
        } catch {
            Write-Host "- $Name is installed but the version command failed: $($_.Exception.Message)"
        }
    } else {
        if ($InstallHint) {
            Write-Host "( $Name is NOT installed. You can install it with: $InstallHint )"
        } else {
            Write-Host "( $Name is NOT installed. )"
        }
    }
}

function Invoke-Update {
    param($Name, $Binary, $UpdateCommand, $InstallHint)

    if (Get-Command $Binary -ErrorAction SilentlyContinue) {
        Write-Host "- Updating $Name..."
        try {
            $updateOutput = Invoke-Expression "$UpdateCommand 2>&1" | Out-String
            if (-not [string]::IsNullOrWhiteSpace($updateOutput)) {
                Write-Host $updateOutput.Trim()
            }
            Write-Host "  Update completed."
        } catch {
            Write-Host $_.Exception.Message
            Write-Host "! Failed to update $Name."
        }
    } else {
        $hint = if ($InstallHint) { $InstallHint } else { "install instructions unavailable" }
        Write-Host "! $Name is not installed. Run: $hint"
    }
    Write-Host ""
}

switch ($Subcommand) {
    "check" {
        if ($Arguments.Count -lt 2) {
            Write-Error "check requires a name and version command"
            Show-Usage
            exit 1
        }
        $name = $Arguments[0]
        $versionCommand = $Arguments[1]
        $installHint = if ($Arguments.Count -gt 2) { $Arguments[2] } else { "" }
        Invoke-Check -Name $name -VersionCommand $versionCommand -InstallHint $installHint
    }

    "update" {
        if ($Arguments.Count -lt 3) {
            Write-Error "update requires a name, binary, and update command"
            Show-Usage
            exit 1
        }
        $name = $Arguments[0]
        $binary = $Arguments[1]
        $updateCommand = $Arguments[2]
        $installHint = if ($Arguments.Count -gt 3) { $Arguments[3] } else { "" }
        Invoke-Update -Name $name -Binary $binary -UpdateCommand $updateCommand -InstallHint $installHint
    }

    "check-all" {
        if ($Arguments.Count -lt 1) {
            Write-Error "check-all requires agent definitions"
            Show-Usage
            exit 1
        }
        $agentsData = $Arguments[0]

        Clear-Host
        Write-Host "Checking CLI coding agents...`n"

        $agentsData -split "`n" | ForEach-Object {
            $parts = $_ -split '\|', 4
            if ($parts.Count -ge 3 -and $parts[0]) {
                $name = $parts[0]
                $command = $parts[1]
                $versionArgs = $parts[2]
                $installHint = if ($parts.Count -gt 3) { $parts[3] } else { "" }
                $versionCommand = "$command $versionArgs".Trim()
                Invoke-Check -Name $name -VersionCommand $versionCommand -InstallHint $installHint
            }
        }
    }

    "update-all" {
        if ($Arguments.Count -lt 2) {
            Write-Error "update-all requires agent definitions and active agent IDs"
            Show-Usage
            exit 1
        }
        $agentsData = $Arguments[0]
        $activeIds = $Arguments[1]

        if ([string]::IsNullOrEmpty($activeIds)) {
            Write-Host "No coding agents are enabled. Enable them via Preferences > Tools > LLM Brains."
            exit 0
        }

        Clear-Host
        Write-Host "Updating enabled coding agents...`n"

        $activeIdsList = ",$activeIds,"
        $agentsData -split "`n" | ForEach-Object {
            $parts = $_ -split '\|', 6
            if ($parts.Count -ge 5 -and $parts[0]) {
                $id = $parts[0]
                if ($activeIdsList -like "*,$id,*") {
                    $name = $parts[1]
                    $command = $parts[2]
                    $updateHint = $parts[4]
                    $installHint = if ($parts.Count -gt 5) { $parts[5] } else { "" }
                    Invoke-Update -Name $name -Binary $command -UpdateCommand $updateHint -InstallHint $installHint
                }
            }
        }

        Write-Host "All done."
    }

    default {
        Write-Error "Unknown subcommand: $Subcommand"
        Show-Usage
        exit 1
    }
}
